package nl.knaw.huygens.textmod.core.lucene;

import nl.knaw.huygens.textmod.core.text.DefaultTokenTextHandler;
import nl.knaw.huygens.textmod.core.text.TextAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.junit.Assert;

import java.io.IOException;

public abstract class AbstractFilterTest {

  protected abstract Analyzer getAnalyzer();

  protected void doTest(String input, String expected) {
    try {
      Analyzer luceneAnalyzer = getAnalyzer();
      TestHandler handler = new TestHandler();
      TextAnalyzer.newInstance(luceneAnalyzer, handler).analyze(input);
      Assert.assertEquals(expected, handler.getResult());
    } catch (IOException e) {
      Assert.fail(e.getMessage());
    }
  }

  private static class TestHandler extends DefaultTokenTextHandler {
    private StringBuilder builder;

    public TestHandler() {
      builder = new StringBuilder("|");
    }

    @Override
    public void accept(String text) {
      builder.append(text).append("|");
    }

    public String getResult() {
      return builder.toString();
    }
  }
}
