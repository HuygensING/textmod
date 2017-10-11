package nl.knaw.huygens.topmod.core.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.util.CharTokenizer;

import java.util.EnumSet;

/**
 * Lucene analyzer, implementing:<ul>
 * <li>tokenization</li>
 * <li>conversion to lower case</li>
 * <li>handling of length, digits</li>
 * </ul>
 */
public final class LuceneAnalyzer extends Analyzer {

  private static final int MIN_LENGTH = 3;

  private final EnumSet<FilterOption> options;

  public LuceneAnalyzer(EnumSet<FilterOption> options) {
    this.options = EnumSet.copyOf(options); // defensive copy
  }

  public LuceneAnalyzer(FilterOption option) {
    this(EnumSet.of(option));
  }

  public LuceneAnalyzer() {
    this(FilterOptions.generic());
  }

  @Override
  protected TokenStreamComponents createComponents(String fieldName) {
    Tokenizer tokenizer = newTokenizer();

    // Language independent processing
    TokenFilter filter = new LowerCaseFilter(tokenizer);
    if (options.contains(FilterOption.BASIC)) {
      filter = new BaseFilter(filter, MIN_LENGTH);
    }
    if (options.contains(FilterOption.DIACR)) {
      filter = new ASCIIFoldingFilter(filter);
    }

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
