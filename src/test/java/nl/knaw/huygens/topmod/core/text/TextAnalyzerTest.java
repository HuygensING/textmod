package nl.knaw.huygens.topmod.core.text;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.junit.Assert;
import org.junit.Test;

public class TextAnalyzerTest {

  @Test
  public void testSimpleText() throws IOException {
    Analyzer luceneAnalyzer = new SimpleAnalyzer();
    ListTokenTextHandler handler = new ListTokenTextHandler();
    TextAnalyzer.newInstance(luceneAnalyzer, handler).analyze("\t Hugo de Groot \n");

    List<String> tokens = handler.getTokens();
    Assert.assertEquals(3, tokens.size());
    Assert.assertEquals("hugo", tokens.get(0));
    Assert.assertEquals("de", tokens.get(1));
    Assert.assertEquals("groot", tokens.get(2));
  }

}
