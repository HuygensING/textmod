package nl.knaw.huygens.topmod.core.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.junit.Test;

public class ASCIIFoldingFilterTest extends AbstractFilterTest {

  @Override
  protected Analyzer getAnalyzer() {
    return new LuceneAnalyzer(FilterOption.DIACR);
  }

  @Test
  public void diacriticsAreRemoved() {
    doTest("d'après ce qui précède", "|d|apres|ce|qui|precede|");
  }

  @Test
  public void mathTermsArePreserved() {
    doTest("ABC = ½ ABHG", "|abc|abhg|");
  }

}
