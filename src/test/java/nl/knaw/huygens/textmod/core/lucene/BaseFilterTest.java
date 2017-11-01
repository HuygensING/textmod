package nl.knaw.huygens.textmod.core.lucene;

import nl.knaw.huygens.textmod.core.lucene.FilterOption;
import nl.knaw.huygens.textmod.core.lucene.LuceneAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.junit.Test;

public class BaseFilterTest extends AbstractFilterTest {

  @Override
  protected Analyzer getAnalyzer() {
    return new LuceneAnalyzer(FilterOption.BASIC);
  }

  @Test
  public void tokensAreLowercase() {
    doTest("Christiaan Huygens van Zuylichem", "|christiaan|huygens|van|zuylichem|");
  }

  @Test
  public void shortWordsAreRemoved() {
    doTest("a bb ccc dddd", "|ccc|dddd|");
  }

  @Test
  public void tokensWithDigitsAreRemoved() {
    doTest("aaa a33 3a3 a33 333", "|aaa|");
  }

  @Test
  public void tokensWithGreekAreRemoved() {
    doTest("aaa aaϕ  aϕa ϕaa aϕϕ  ϕaϕ  ϕϕ a ϕϕϕ ", "|aaa|");
  }

}
