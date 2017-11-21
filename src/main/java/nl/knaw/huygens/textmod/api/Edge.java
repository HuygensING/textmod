package nl.knaw.huygens.textmod.api;

public class Edge {

  private final String[] nodes;
  private long weight;

  public Edge(String[] nodes, long weight) {
    this.nodes = nodes;
    this.weight = weight;
  }

  public String[] getNodes() {
    return nodes;
  }

  public long getWeight() {
    return weight;
  }

  public void setWeight(long weight) {
    this.weight = weight;
  }

}
