package nl.knaw.huygens.topmod.core;

import nl.knaw.huygens.topmod.core.text.Language;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
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

  @Test
  public void testConstruction() throws IOException {
    String input = "locorum;locorum;233\n" //
        + "locorumque;locorum;2\n" //
        + "locos;locos;32\n" //
        + "loculi;loculi;2\n" //
        + "loculis;loculis;10\n" //
        + "loculos;loculos;5\n" //
        + "locum;locum;839\n" //
        + "locumque;locum;3\n" //
        + "locuples;locuples;7\n" //
        + "locupletare;locupletare;8\n";
    StringReader reader = new StringReader(input);
    TermIndex index = getTermIndex();
    index.addTermFile(reader, Language.LATIN);

    index.openForReading();
    List<WeightedTerm> terms = index.denormalize("locum");
    index.closeAfterReading();

    Assert.assertEquals(2, terms.size());
    List<String> texts = terms.stream().map(WeightedTerm::getText).collect(Collectors.toList());
    Assert.assertTrue(texts.contains("locum"));
    Assert.assertTrue(texts.contains("locumque"));
  }

}
