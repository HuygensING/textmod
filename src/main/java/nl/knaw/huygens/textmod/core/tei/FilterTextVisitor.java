package nl.knaw.huygens.textmod.core.tei;

import java.io.IOException;
import java.util.Set;

import com.google.common.collect.Sets;

import nl.knaw.huygens.tei.Element;
import nl.knaw.huygens.tei.ElementHandler;
import nl.knaw.huygens.tei.Traversal;
import nl.knaw.huygens.tei.XmlContext;
import nl.knaw.huygens.tei.handlers.FilterElementHandler;
import nl.knaw.huygens.textmod.core.text.TextAnalyzer;
import nl.knaw.huygens.textmod.core.text.TokenTextHandler;

public class FilterTextVisitor<T extends TokenTextHandler> extends DefaultTextVisitor {

  public static <U extends TokenTextHandler> FilterTextVisitor<U> newInstance(TextAnalyzer<U> analyzer) {
    return new FilterTextVisitor<U>(analyzer);
  }

  private static Set<String> FILTERED_DIV_TYPES = Sets.newHashSet("comment", "notes", "para", "provenance");

  private final TextAnalyzer<T> textAnalyzer;

  private FilterTextVisitor(TextAnalyzer<T> analyzer) {
    textAnalyzer = analyzer;
    setDefaultElementHandler(new AnalysisHandler());
    addElementHandler(new FilterElementHandler<XmlContext>(), "opener", "closer", "formula");
  }

  private class AnalysisHandler implements ElementHandler<XmlContext> {

    private boolean reject(Element element) {
      return element.hasName("div") && FILTERED_DIV_TYPES.contains(element.getType());
    }

    @Override
    public Traversal enterElement(Element element, XmlContext context) {
      if (reject(element)) {
        return Traversal.STOP;
      } else {
        context.openLayer();
        return Traversal.NEXT;
      }
    }

    @Override
    public Traversal leaveElement(Element element, XmlContext context) {
      if (!reject(element)) {
        try {
          textAnalyzer.analyze(context.closeLayer());
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
      return Traversal.NEXT;
    }
  }

}
