package nl.knaw.huygens.textmod.cocit;

import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;
import com.google.common.collect.TreeBasedTable;
import nl.knaw.huygens.tei.Document;
import nl.knaw.huygens.textmod.api.Cocitation;
import nl.knaw.huygens.textmod.api.CocitationResult;
import nl.knaw.huygens.textmod.api.Cocitations;
import nl.knaw.huygens.textmod.api.Edge;
import nl.knaw.huygens.textmod.api.Graph;
import nl.knaw.huygens.textmod.api.XmlDocument;
import nl.knaw.huygens.textmod.core.tei.Documents;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CocitationAnalyzer {

  /**
   * Prevent combinatorial explosion
   */
  private static final int MAX_ITEMS_IN_SEGMENT = 10;

  public CocitationAnalyzer() {}

  public Graph asGraph(Set<XmlDocument> documents) {
    return convertToGraph(analyze(documents));
  }

  public CocitationResult asFull(Set<XmlDocument> documents) {
    return analyze(documents);
  }

  public List<Cocitation> asSimple(Set<XmlDocument> documents) {
    return analyze(documents).getOverall();
  }

  private CocitationResult analyze(Set<XmlDocument> documents) {
    CocitationResult result = new CocitationResult();
    if (documents != null) {
      result.setDetail(documents.stream().map(this::calculateCocitations).collect(Collectors.toSet()));
      result.setOverall(combine(result.getDetail()));
    }
    return result;
  }

  private Cocitations calculateCocitations(XmlDocument doc) {
    Document document = Documents.newDocument(doc.getXml());
    Set<String> correspondents = getCorrespondents(document);
    CitationTeiVisitor visitor = new CitationTeiVisitor(correspondents);
    document.accept(visitor);
    List<Set<String>> citations = visitor.getCitations();

    Table<String, String, Long> table = TreeBasedTable.create();
    for (Set<String> citation : citations) {
      String[] items = citation.toArray(new String[0]);
      int n = items.length;
      if (n <= MAX_ITEMS_IN_SEGMENT) {
        for (int i = 0; i < n; i++) {
          String col = items[i];
          for (int j = i + 1; j < n; j++) {
            String row = items[j];
            if (col.compareTo(row) > 0) {
              updateTable(table, row, col, 1);
            } else {
              updateTable(table, col, row, 1);
            }
          }
        }
      }
    }

    return new Cocitations(doc.getId(), convertTable(table));
  }

  private Set<String> getCorrespondents(Document document) {
    return document.getElementsByTagName("meta").stream().filter(e -> e.hasType("sender") || e.hasType("recipient")).map(e -> e.getAttribute("value")).filter(v -> !v.isEmpty() && !v.equals("?"))
        .collect(Collectors.toSet());
  }

  private void updateTable(Table<String, String, Long> table, String row, String col, long count) {
    Long value = table.get(row, col);
    if (value == null) {
      table.put(row, col, count);
    } else {
      table.put(row, col, value + count);
    }
  }

  private List<Cocitation> convertTable(Table<String, String, Long> table) {
    return table.cellSet().stream().map(this::cellToCocitation).collect(Collectors.toList());
  }

  private Cocitation cellToCocitation(Cell<String, String, Long> cell) {
    return new Cocitation(cell.getValue(), cell.getRowKey(), cell.getColumnKey());
  }

  private List<Cocitation> combine(Set<Cocitations> items) {
    Table<String, String, Long> table = TreeBasedTable.create();
    for (Cocitations item : items) {
      item.stream().forEach(c -> updateTable(table, c.items[0], c.items[1], c.count));
    }
    return convertTable(table);
  }

  private Graph convertToGraph(CocitationResult data) {
    Graph graph = new Graph("undirected");
    for (Cocitation cocitation : data.getOverall()) {
      graph.addNode(cocitation.items[0]);
      graph.addNode(cocitation.items[1]);
      graph.addEdge(new Edge(cocitation.items, cocitation.count));
    }
    return graph;
  }

}
