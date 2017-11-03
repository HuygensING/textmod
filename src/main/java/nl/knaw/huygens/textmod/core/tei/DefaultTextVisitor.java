package nl.knaw.huygens.textmod.core.tei;

import nl.knaw.huygens.tei.DelegatingVisitor;
import nl.knaw.huygens.tei.Element;
import nl.knaw.huygens.tei.Traversal;
import nl.knaw.huygens.tei.XmlContext;
import nl.knaw.huygens.tei.handlers.DefaultElementHandler;

public class DefaultTextVisitor extends DelegatingVisitor<XmlContext> {

  public DefaultTextVisitor() {
    super(new XmlContext());
    addElementHandler(new LinebreakHandler(), "lb");
    addElementHandler(new PagebreakHandler(), "pb");
  }

  @Override
  public String getResult() {
    // Remove brackets and parentheses
    return getContext().getResult()
                       .replaceAll("[\\[\\]\\(\\)]", "");
  }

  private static class LinebreakHandler extends DefaultElementHandler<XmlContext> {
    @Override
    public Traversal enterElement(Element element, XmlContext context) {
      context.addLiteral('\n');
      return Traversal.NEXT;
    }
  }

  private static class PagebreakHandler extends DefaultElementHandler<XmlContext> {
    @Override
    public Traversal enterElement(Element element, XmlContext context) {
      if (!element.hasAttribute("break", "no")) {
        context.addLiteral(' ');
      }
      return Traversal.NEXT;
    }
  }

}
