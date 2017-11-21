package nl.knaw.huygens.textmod.api;

import com.google.common.collect.Lists;

import java.util.List;

public class Graph {

  private final String type;
  private List<Node> nodes;
  private List<Edge> edges;

  public Graph(String type) {
    this.type = type;
    nodes = Lists.newArrayList();
    edges = Lists.newArrayList();
  }

  public String getType() {
    return type;
  }

  public List<Node> getNodes() {
    return nodes;
  }

  public void setNodes(List<Node> nodes) {
    this.nodes = nodes;
  }

  public List<Edge> getEdges() {
    return edges;
  }

  public void setEdges(List<Edge> edges) {
    this.edges = edges;
  }

}
