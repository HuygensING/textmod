package nl.knaw.huygens.textmod.core.lucene;

import nl.knaw.huygens.textmod.core.lucene.LuceneUtils;
import org.apache.lucene.search.Query;
import org.junit.Assert;
import org.junit.Test;

public class LuceneUtilsTest {

  @Test
  public void testQueryTerm() {
    Query query = LuceneUtils.newTermQuery("field", "text");
    Assert.assertEquals("field:text", query.toString());
  }

}
