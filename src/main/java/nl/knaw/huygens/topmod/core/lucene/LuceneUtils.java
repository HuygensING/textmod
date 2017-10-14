package nl.knaw.huygens.topmod.core.lucene;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;

public class LuceneUtils {

  private static final Logger LOG = LoggerFactory.getLogger(LuceneUtils.class);

  public static Query newTermQuery(String field, String text) {
    return new TermQuery(new Term(field, text));
  }

  public static <T extends Closeable> T closeQuietly(T closeable) {
    if (closeable != null) {
      try {
        closeable.close();
      } catch (IOException e) {
        LOG.error("Error closing {}: {}", closeable.getClass().getName(), e.getMessage());
      }
    }
    return null;
  }

  private LuceneUtils() {
    throw new AssertionError("Non-instantiable class");
  }

}
