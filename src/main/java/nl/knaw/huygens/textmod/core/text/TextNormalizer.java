package nl.knaw.huygens.textmod.core.text;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.io.StringReader;
import java.util.Objects;
import java.util.function.Function;

/**
 * Normalizes text using a specified Lucene analyzer.
 */
public class TextNormalizer implements Function<String, String> {

  private final Analyzer analyzer;

  public TextNormalizer(Analyzer analyzer) {
    this.analyzer = Objects.requireNonNull(analyzer, "analyzer must not be null");
  }

  @Override
  public String apply(String text) {
    return normalize(text);
  }

  public String normalize(String text) {
    try {
      StringBuilder builder = new StringBuilder();
      if (text != null && !text.isEmpty()) {
        TokenStream stream = analyzer.tokenStream(null, new StringReader(text));
        CharTermAttribute attribute = stream.getAttribute(CharTermAttribute.class);

        stream.reset();
        if (stream.incrementToken()) {
          builder.append(attribute.buffer(), 0, attribute.length());
        }
        while (stream.incrementToken()) {
          builder.append(' ').append(attribute.buffer(), 0, attribute.length());
        }
        stream.close();
      }
      return builder.toString();
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

}
