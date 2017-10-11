package nl.knaw.huygens.topmod.core.lucene;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.FilteringTokenFilter;

import java.io.IOException;
import java.lang.Character.UnicodeBlock;

import static java.lang.Character.UnicodeBlock.ARABIC;
import static java.lang.Character.UnicodeBlock.GREEK;
import static java.lang.Character.UnicodeBlock.GREEK_EXTENDED;
import static java.lang.Character.UnicodeBlock.HEBREW;

/**
 * Lucene filter that rejects tokens<ul>
 * <li>with fewer than <code>min</code> characters;</li>
 * <li>containing digits;</li>
 * <li>containing Greek, Hebrew or Arabic characters.</li>
 * </ul>
 */
public class BaseFilter extends FilteringTokenFilter {

  private final CharTermAttribute termAttribute;
  private final int min;

  public BaseFilter(TokenStream input, int min) {
    super(input);
    termAttribute = addAttribute(CharTermAttribute.class);
    this.min = min;
  }

  @Override
  protected boolean accept() throws IOException {
    int n = termAttribute.length();
    if (n < min) {
      return false;
    }
    char[] chars = termAttribute.buffer();
    for (int i = 0; i < n; i++) {
      char ch = chars[i];
      if (Character.isDigit(ch) || isIgnoredChar(ch)) {
        return false;
      }
    }
    return true;
  }

  public static boolean isIgnoredChar(char c) {
    UnicodeBlock block = UnicodeBlock.of(c);
    return block == GREEK || block == GREEK_EXTENDED || block == HEBREW || block == ARABIC;
  }

}
