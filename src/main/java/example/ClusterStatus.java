package example;

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
