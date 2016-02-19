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
    Request request = new Request.Builder()
        .url(httpsURL)
        .header("Authorization", basicAuth)
        .build();

    String statusResponse = client.newCall(request).execute().body().string();

    System.out.println("status = " + statusResponse);

    Gson gson;
    GsonBuilder gsonBuilder = new GsonBuilder();
    gson = gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

    Retrofit retrofit = new Retrofit.Builder()
        .client(client)
        .baseUrl(httpsURL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build();

    ClusterQuery query = retrofit.create(ClusterQuery.class);

    Call<ClusterStatus> queryCall = query.getStatus(basicAuth);
    ClusterStatus clusterStatus = queryCall.execute().body();
    System.out.println(clusterStatus.clusterName);
    System.out.println(clusterStatus.status);
  }
}
