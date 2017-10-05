package nl.knaw.huygens.topmod.core.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.util.CharTokenizer;

/**
 * Lucene analyzer, implementing:<ul>
 * <li>tokenization</li>
 * <li>conversion to lower case</li>
 * </ul>
 */
public final class TMAnalyzer extends Analyzer {

  public TMAnalyzer() {}

  @Override
  protected TokenStreamComponents createComponents(String fieldName) {
    Tokenizer tokenizer = newTokenizer();
    TokenFilter filter = new LowerCaseFilter(tokenizer);
    return new TokenStreamComponents(tokenizer, filter);
  }

  private Tokenizer newTokenizer() {
    // Lucene 6: Tokenizer tokenizer = CharTokenizer.fromTokenCharPredicate(Character::isLetterOrDigit);
    Tokenizer tokenizer = new CharTokenizer() {
      @Override
      protected boolean isTokenChar(int c) {
        return Character.isLetterOrDigit(c);
      }
    };
    return tokenizer;
  }

}
