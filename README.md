# Connecting to Elasticsearch with SSL/TLS

For Java applications, ElasticSearch uses a serialisation network protocol with the Transport client that supports handling SSL/TLS connections

> Refer to: https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/transport-client.html

We want to access our deployment with HTTPS and without using the Apache Http Client as the connection broker; since we can run into problems with wildcard certificates:-

> Refer to: (https://issues.apache.org/jira/browse/HTTPCLIENT-1613).

Instead, we can access the ElasticSearch REST API using the OKHttp Java client

> Refer to: https://square.github.io/okhttp/

On top of this we can build our own API with Retrofit and map the responses onto Java classes

> Refer to: https://square.github.io/retrofit/

### Using the REST API with JDK 7 or 8

The Elasticsearch 1.4.2 update also moves to Java 8, so check that you are running a compatible version

> Refer to: https://www.elastic.co/support/matrix#show_jvm

For our Java project, we are showing the most basic Gradle build, with dependencies used with this example are downloaded from jcenter:-

``` gradle
plugins {
  id "java"
}

repositories {
  jcenter()
}

dependencies {
  compile(
          "com.squareup.retrofit2:retrofit:2.0.0-beta4",
          "com.squareup.retrofit2:converter-gson:2.0.0-beta4",
          "com.google.code.gson:gson:2.5"
  )
}
```

This Elasticsearch query "_cluster/health?pretty" has the JSON response format:-

``` json
{
  "cluster_name" : "ecstatic-elasticsearch-73",
  "status" : "green",
  "timed_out" : false,
  "number_of_nodes" : 3,
  "number_of_data_nodes" : 3,
  "active_primary_shards" : 0,
  "active_shards" : 0,
  "relocating_shards" : 0,
  "initializing_shards" : 0,
  "unassigned_shards" : 0,
  "delayed_unassigned_shards" : 0,
  "number_of_pending_tasks" : 0,
  "number_of_in_flight_fetch" : 0
}
```

The Java Class we are mapping onto looks like this if we are just interested in a few attributes:-

``` java
public class ClusterStatus {

  public final String clusterName;
  public final String status;
  public final boolean timedOut;
  public final int numberOfNodes;

  public ClusterStatus(String clusterName, String status, boolean timedOut, int numberOfNodes) {
    this.clusterName = clusterName;
    this.status = status;
    this.timedOut = timedOut;
    this.numberOfNodes = numberOfNodes;
  }
}
```

An interface method is needed for Retrofit to make each query request:-

``` java
public interface ClusterQuery {
  @GET("_cluster/health?pretty") Call<ClusterStatus> getStatus(
      @Header("Authorization") String authorization);
}
```

The @GET annotated method getStatus makes the request and returns a ClusterStatus object

Every request for Elasticsearch requires authentication. Using @Header to pass the "Authorization" credentials is one simple approach

Let's create the steps needed to invoke the query interface

First, create the Credentials from a valid Elasticsearch username and password:-

``` java
String basicAuth = Credentials.basic("myadmin", "FIXME!");
String httpsURL = "https://FIXME!";
```

Gson is the library dependency we are using to convert the JSON response to Java

> Refer to: https://github.com/google/gson

Second, setup a Gson configuration will match the JSON response naming format to Java camelCase:-

``` java
GsonBuilder gsonBuilder = new GsonBuilder();
gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
Gson gson = gsonBuilder.create();
GsonConverterFactory converterFactory = GsonConverterFactory.create(gson);
```

Third, create the Retrofit instance by assigning the Elasticsearch server url and setting a Gson converter through a builder:-

``` java
Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
retrofitBuilder.baseUrl(httpsURL).addConverterFactory(converterFactory);
Retrofit retrofit = retrofitBuilder.build();
```

Finally, we are ready to make requests. Retrofit works by implementing the ClusterQuery interface with an OkHttp client underneath

The ClusterQuery.getStatus method takes the credentials in the correct format and returns a Callable instance to invoke the request

Retrofit is designed that the synchronous or asynchronous HTTP call is an actual return type and must be invoked to fetch the body payload

``` java
ClusterQuery query = retrofit.create(ClusterQuery.class);
Call<ClusterStatus> queryCall = query.getStatus(basicAuth);
ClusterStatus clusterStatus = queryCall.execute().body();
```
The final resulting ClusterStatus is just a normal instance of that class