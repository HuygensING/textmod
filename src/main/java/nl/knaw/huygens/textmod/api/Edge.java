package nl.knaw.huygens.textmod.api;

public class Edge {

  private final int[] nodes;
  private int weight;

  public Edge(int[] nodes) {
    this.nodes = nodes;
    weight = 1;
  }

  public Edge(int source, int target) {
    this(new int[] { source, target });
  }

  public int[] getNodes() {
    return nodes;
  }

  public int getWeight() {
    return weight;
  }

  public void setWeight(int weight) {
    this.weight = weight;
  }

}
