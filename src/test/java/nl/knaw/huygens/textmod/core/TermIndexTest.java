package nl.knaw.huygens.textmod.core;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TermIndexTest {

  private static class TestTermIndex extends TermIndex {
    private final Directory ramDirectory;

    public TestTermIndex() {
      super(null);
      ramDirectory = new RAMDirectory();
    }

    @Override
    protected Directory getDirectory() throws IOException {
      return ramDirectory;
    }

    @Override
    protected void closeDirectory(Directory directory) {}
  }

  private TermIndex getTermIndex() {
    return new TestTermIndex();
  }

  private Reader input(String... lines) {
    // Concatenate lines, separated and terminated by newline
    String s = Arrays.asList(lines)
                     .stream()
                     .collect(Collectors.joining("\n", "", "\n"));
    return new StringReader(s);
  }

  private Reader defaultInput() {
    return input(
      "locorum;locorum;233",
      "locorumque;locorum;2",
      "locos;locos;32",
      "loculi;loculi;2",
      "loculis;loculis;10",
      "loculos;loculos;5",
      "locum;locum;839",
      "locumque;locum;3",
      "locuples;locuples;7",
      "locupletare;locupletare;8"
    );
  }

  @Test
  public void testNormalization() throws IOException {
    TermIndex index = getTermIndex();
    index.addTermFile(defaultInput(), Language.LATIN);

    try {
      index.openForReading();
      Assert.assertEquals("locorum", index.normalize("locorumque"));
      Assert.assertEquals("locorum", index.normalize("locorum"));
      Assert.assertEquals("unknown", index.normalize("unknown"));
    } finally {
      index.closeAfterReading();
    }
  }

  @Test
  public void testDenormalization() throws IOException {
    TermIndex index = getTermIndex();
    index.addTermFile(defaultInput(), Language.LATIN);

    index.openForReading();
    List<WeightedTerm> terms = index.denormalize("locum");
    index.closeAfterReading();

    Assert.assertEquals(2, terms.size());
    List<String> texts = terms.stream()
                              .map(WeightedTerm::getText)
                              .collect(Collectors.toList());
    Assert.assertTrue(texts.contains("locum"));
    Assert.assertTrue(texts.contains("locumque"));
  }

}
