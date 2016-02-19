# elasticsearch-01

https://square.github.io/okhttp/
https://github.com/square/retrofit

---

ElasticSearch uses Java serialisation with [Transport](https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/transport-client.html).

Instead, to access with HTTPS and without using Apache Http Client and [issues](https://issues.apache.org/jira/browse/HTTPCLIENT-1613).

Simple ElasticSearch HTTP API example to map the "_cluster/health?pretty" response.

Status response mapped to instance of Java class with gson.

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