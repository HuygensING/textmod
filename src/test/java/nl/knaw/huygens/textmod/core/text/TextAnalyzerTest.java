package nl.knaw.huygens.textmod.core.text;

import nl.knaw.huygens.textmod.core.text.TextAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class TextAnalyzerTest {

  @Test
  public void testParsing() throws IOException {
    List<String> tokens = TextAnalyzer.parse(new SimpleAnalyzer(), "\t Hugo de Groot \n");
    Assert.assertEquals(3, tokens.size());
    Assert.assertEquals("hugo", tokens.get(0));
    Assert.assertEquals("de", tokens.get(1));
    Assert.assertEquals("groot", tokens.get(2));
  }

}
