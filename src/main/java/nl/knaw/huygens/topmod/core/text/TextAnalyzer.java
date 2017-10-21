package nl.knaw.huygens.topmod.core.text;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

/**
 * Base class for analyzing text token by token.
 * It allows for segments characterized by an id.
 */
public class TextAnalyzer<T extends TokenTextHandler> {

  public static <U extends TokenTextHandler> TextAnalyzer<U> newInstance(Analyzer luceneAnalyzer, U handler) {
    return new TextAnalyzer<U>(luceneAnalyzer, handler);
  }

  public static <U extends TokenTextHandler> U analyze(Analyzer luceneAnalyzer, U handler, String text) {
    try {
      newInstance(luceneAnalyzer, handler).analyze(text);
      return handler;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static List<String> parse(Analyzer luceneAnalyzer, String text) {
    return analyze(luceneAnalyzer, new ListTokenTextHandler(), text).getTokens();
  }

  private final Analyzer luceneAnalyzer;
  private final T tokenTextHandler;

  private TextAnalyzer(Analyzer luceneAnalyzer, T tokenTextHandler) {
    this.luceneAnalyzer = luceneAnalyzer;
    this.tokenTextHandler = tokenTextHandler;
  }

  public T getHandler() {
    return tokenTextHandler;
  }

  public void analyze(String segmentId, String text) throws IOException {
    try (TokenStream stream = luceneAnalyzer.tokenStream(null, new StringReader(text))) {
      CharTermAttribute attribute = stream.getAttribute(CharTermAttribute.class);
      stream.reset();
      tokenTextHandler.newSegment(segmentId);
      while (stream.incrementToken()) {
        String term = new String(attribute.buffer(), 0, attribute.length());
        tokenTextHandler.handle(term);
      }
      tokenTextHandler.endSegment();
    }
  }

  public void analyze(String text) throws IOException {
    analyze("", text);
  }

}
