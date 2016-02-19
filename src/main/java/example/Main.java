package example;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Main {

  public static void main(String[] args) throws IOException {

    String basicAuth = Credentials.basic("myadmin", "FIXME!");

    String httpsURL = "FIXME!";

    OkHttpClient client = new OkHttpClient();
    Request.Builder requestBuilder = new Request.Builder();
    requestBuilder.url(httpsURL).header("Authorization", basicAuth);
    Request request = requestBuilder.build();
    String statusResponse = client.newCall(request).execute().body().string();

    System.out.println("status = " + statusResponse);

    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
    Gson gson = gsonBuilder.create();
    GsonConverterFactory converterFactory = GsonConverterFactory.create(gson);

    Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
    retrofitBuilder.baseUrl(httpsURL).client(client).addConverterFactory(converterFactory).build();
    Retrofit retrofit = retrofitBuilder.build();
    ClusterQuery query = retrofit.create(ClusterQuery.class);

    Call<ClusterStatus> queryCall = query.getStatus(basicAuth);
    ClusterStatus clusterStatus = queryCall.execute().body();
    System.out.println(clusterStatus.clusterName);
    System.out.println(clusterStatus.status);
  }
}
