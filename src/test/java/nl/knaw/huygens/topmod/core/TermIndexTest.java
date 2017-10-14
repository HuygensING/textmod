package nl.knaw.huygens.topmod.core;

import nl.knaw.huygens.topmod.core.text.Language;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
    protected void closeDirectory() {
      directory = null;
    }
  }

  private TermIndex getTermIndex() {
    return new TestTermIndex();
  }

  @Test
  public void testConstruction() throws IOException {
    TermIndex index = getTermIndex();
    index.addTermFile(new File("terms-nl.txt"), Language.DUTCH);
    index.addTermFile(new File("terms-fr.txt"), Language.FRENCH);
    index.addTermFile(new File("terms-la.txt"), Language.LATIN);

    index.openForReading();
    List<String> terms = index.denormalize("koning");
    index.closeAfterReading();

    Assert.assertEquals(3, terms.size());
    Assert.assertTrue(terms.contains("coninghfr"));
    Assert.assertTrue(terms.contains("coninghla"));
    Assert.assertTrue(terms.contains("coninghnl"));
  }

}
