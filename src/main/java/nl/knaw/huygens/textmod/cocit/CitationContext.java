package nl.knaw.huygens.textmod.cocit;

import nl.knaw.huygens.tei.XmlContext;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Keeps track of citations, allowing the document to be segmented.
 */
public class CitationContext extends XmlContext {

  private final Set<String> excludedItems;
  private final List<Set<String>> items;
  private Set<String> segItems;
  private int segLevel = 0;

  public CitationContext(Set<String> excludedItems) {
    this.excludedItems = excludedItems;
    items = new ArrayList<Set<String>>();
  }

  public List<Set<String>> getCitations() {
    return items;
  }

  public void enterSegment() {
    if (segLevel == 0) {
      segItems = new HashSet<String>();
    }
    segLevel++;
  }

  public void leaveSegment() {
    segLevel--;
    if (segLevel == 0) {
      if (segItems.size() > 1) {
        items.add(segItems);
      }
      segItems = null;
    }
  }

  public void addItem(String item) {
    if (segItems != null && !item.isEmpty() && !excludedItems.contains(item)) {
      segItems.add(item);
    }
  }

}
