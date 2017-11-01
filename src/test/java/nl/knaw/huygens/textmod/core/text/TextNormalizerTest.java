package nl.knaw.huygens.textmod.core.text;

import nl.knaw.huygens.textmod.core.lucene.LuceneAnalyzer;
import nl.knaw.huygens.textmod.core.text.TextNormalizer;
import org.apache.lucene.analysis.Analyzer;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TextNormalizerTest {

  private static TextNormalizer normalizer;

  @BeforeClass
  public static void setupAnalyzer() {
    Analyzer analyzer = new LuceneAnalyzer();
    normalizer = new TextNormalizer(analyzer);
  }

  private void testCase(String expected, String text) {
    Assert.assertEquals(expected, normalizer.normalize(text));
  }

  @Test
  public void nullString() {
    testCase("", null);
  }

  @Test
  public void emptyString() {
    testCase("", "");
  }

  @Test
  public void singleWord() {
    testCase("instituut", "instituut");
  }

  @Test
  public void twoWords() {
    testCase("huygens ing", "Huygens ING!");
  }

  @Test
  public void simpleNumbers() {
    testCase("", "1234 5432");
  }

  @Test
  public void mixedNumbersAndLetters() {
    testCase("tje", "aa4-tje");
  }

  @Test
  public void nonBrakingSpace() {
    testCase("non breaking space", "non\u00A0breaking space");
  }

  @Test
  public void textWithSpecials() {
    testCase("txt txt txt txt txt xtx xtx", "txt @ # $ _ @@ #@ $@ _@ txt@ txt# txt$ txt_ xtx#xtx");
  }

}
