package nl.knaw.huygens.textmod.core.text;

import nl.knaw.huygens.textmod.core.lucene.FilterOption;
import nl.knaw.huygens.textmod.core.lucene.LuceneAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class TextAnalyzerTest {

  @Test
  public void testParsing() throws IOException {
    List<String> tokens = TextAnalyzer.parse(new SimpleAnalyzer(), "\t Hugo de Groot \n");
    Assert.assertEquals(Arrays.asList("hugo", "de", "groot"), tokens);
  }

  @Test
  public void testLuceneAnalyzer() throws IOException {
    Analyzer luceneAnalyzer = new LuceneAnalyzer(FilterOption.BASIC);
    List<String> tokens = TextAnalyzer.parse(luceneAnalyzer, "a bb ccc ϕησί bc3x");
    Assert.assertEquals(Arrays.asList("ccc"), tokens);
  }

}
