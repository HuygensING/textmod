package nl.knaw.huygens.textmod.core.tei;

import nl.knaw.huygens.textmod.core.text.ListTokenTextHandler;
import nl.knaw.huygens.textmod.core.text.TextAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.junit.Assert;
import org.junit.Test;

import java.util.stream.Collectors;

public class FilterTextVisitorTest {

  private String processText(String xml) {
    ListTokenTextHandler handler = new ListTokenTextHandler();
    TextAnalyzer<ListTokenTextHandler> analyzer = TextAnalyzer.newInstance(new SimpleAnalyzer(), handler);
    Documents.visitXml(xml, FilterTextVisitor.newInstance(analyzer));
    return handler.stream()
                  .collect(Collectors.joining(" "));
  }

  @Test
  public void testNoAnaAttribute() {
    String xml = "<body><p>one</p><p>two</p><p>three</p></body>";
    Assert.assertEquals("one two three", processText(xml));
  }

  @Test
  public void testOpenerAnaAttribute() {
    String xml = "<body><p ana=\"#opener\">one</p><p>two</p><p>three</p></body>";
    Assert.assertEquals("two three", processText(xml));
  }

  @Test
  public void testCloserAnaAttribute() {
    String xml = "<body><p>one</p><p>two</p><p ana=\"#closer\">three</p></body>";
    Assert.assertEquals("one two", processText(xml));
  }

  @Test
  public void testUndefinedAnaAttribute() {
    String xml = "<body><p>one</p><p ana=\"#undefined\">two</p><p>three</p></body>";
    Assert.assertEquals("one two three", processText(xml));
  }

}
