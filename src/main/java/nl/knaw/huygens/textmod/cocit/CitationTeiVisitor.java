package nl.knaw.huygens.textmod.cocit;

import com.google.common.collect.Sets;
import nl.knaw.huygens.tei.DelegatingVisitor;
import nl.knaw.huygens.tei.Element;
import nl.knaw.huygens.tei.Text;
import nl.knaw.huygens.tei.TextHandler;
import nl.knaw.huygens.tei.Traversal;
import nl.knaw.huygens.tei.handlers.DefaultElementHandler;

import java.util.List;
import java.util.Set;

public class CitationTeiVisitor extends DelegatingVisitor<CitationContext> {

  private static Set<String> FILTERED_DIV_TYPES = Sets.newHashSet("comment", "notes", "para", "provenance");

  public CitationTeiVisitor(Set<String> correspondents) {
    super(new CitationContext(correspondents));
    addElementHandler(new DivHandler(), "div");
    addElementHandler(new SegmentHandler(), "head", "lg", "list", "p");
    addElementHandler(new PersNameHandler(), "persName", "rs");
    setTextHandler(new NoTextHandler());
  }

  public List<Set<String>> getCitations() {
    return getContext().getCitations();
  }

  private class DivHandler extends DefaultElementHandler<CitationContext> {
    @Override
    public Traversal enterElement(Element element, CitationContext context) {
      return Traversal.stopIf(FILTERED_DIV_TYPES.contains(element.getType()));
    }
  }

  private class SegmentHandler extends DefaultElementHandler<CitationContext> {
    @Override
    public Traversal enterElement(Element element, CitationContext context) {
      context.enterSegment();
      return Traversal.NEXT;
    }

    @Override
    public Traversal leaveElement(Element element, CitationContext context) {
      context.leaveSegment();
      return Traversal.NEXT;
    }
  }

  private class PersNameHandler extends DefaultElementHandler<CitationContext> {
    @Override
    public Traversal enterElement(Element element, CitationContext context) {
      if (element.hasName("persName") || element.hasType("person")) {
        context.addItem(element.getAttribute("key"));
      }
      return Traversal.NEXT;
    }
  }

  private class NoTextHandler implements TextHandler<CitationContext> {
    public Traversal visitText(Text text, CitationContext context) {
      return Traversal.NEXT;
    }
  }

}
