package nl.knaw.huygens.textmod.api;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;

public class Graph {

  private final String type;
  private Set<String> nodes;
  private List<Edge> edges;

  public Graph(String type) {
    this.type = type;
    nodes = Sets.newTreeSet();
    edges = Lists.newArrayList();
  }

  public String getType() {
    return type;
  }

  public Set<String> getNodes() {
    return nodes;
  }

  public void setNodes(Set<String> nodes) {
    this.nodes = nodes;
  }

  public void addNode(String node) {
    nodes.add(node);
  }

  public List<Edge> getEdges() {
    return edges;
  }

  public void setEdges(List<Edge> edges) {
    this.edges = edges;
  }

  public void addEdge(Edge edge) {
    edges.add(edge);
  }

}
