package example;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ClusterQuery {
  @GET("_cluster/health?pretty") Call<ClusterStatus> getStatus(
      @Header("Authorization") String authorization);
}