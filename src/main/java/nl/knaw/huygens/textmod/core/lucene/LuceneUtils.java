package nl.knaw.huygens.textmod.core.lucene;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

public class LuceneUtils {

  public static Query newTermQuery(String field, String text) {
    return new TermQuery(new Term(field, text));
  }

  private LuceneUtils() {
    throw new AssertionError("Non-instantiable class");
  }

}
