package nl.knaw.huygens.textmod.core.text;

import nl.knaw.huygens.textmod.utils.CSVImporter;
import nl.knaw.huygens.textmod.utils.FileUtils;

import java.io.File;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 * A collection of tokens.
 */
public class Tokens {

  private Map<String, Token> tokens;

  public Tokens() {
    tokens = new HashMap<String, Token>();
  }

  public Token get(String key) {
    return tokens.get(key);
  }

  public Set<String> getKeys() {
    return tokens.keySet();
  }

  public void put(Token token) {
    tokens.put(token.getText(), token);
  }

  public void increment(String key, long value) {
    Token token = tokens.get(key);
    if (token == null) {
      token = new Token(key);
      tokens.put(key, token);
    }
    token.increment(value);
  }

  public void increment(String key) {
    increment(key, 1);
  }

  public void increment(String[] keys) {
    for (String key : keys) {
      increment(key, 1);
    }
  }

  public long getTotalTokenCount() {
    return tokens.values()
                 .stream()
                 .mapToLong(Token::getCount)
                 .sum();
  }

  public long getUniqueTokenCount() {
    return tokens.size();
  }

  public long getCountFor(String key) {
    Token token = tokens.get(key);
    return (token != null) ? token.getCount() : 0;
  }

  public double getValueFor(String key) {
    Token token = tokens.get(key);
    return (token != null) ? token.getValue() : 0.0;
  }

  public void handleSorted(TokenHandler handler, Comparator<Token> comparator, long maxSize) {
    stream().sorted(comparator)
            .limit(maxSize)
            .forEach(handler::accept);
  }

  public void handleSorted(TokenHandler handler, Comparator<Token> comparator) {
    handleSorted(handler, comparator, Long.MAX_VALUE);
  }

  public void handle(TokenHandler handler) {
    stream().forEach(handler::accept);
  }

  public Stream<Token> stream() {
    return tokens.values()
                 .stream();
  }

  // --- I/O -------------------------------------------------------------------

  public void write(File file) {
    try (PrintWriter out = FileUtils.printWriterFor(file)) {
      out.printf("--\n-- Token counts\n-- %s\n--\n", new Date());
      handleSorted(t -> out.printf("%s;%d%n", t.getText(), t.getCount()), Token.TEXT_COMPARATOR);
    }
  }

  public void read(File file, long minCount) throws Exception {
    CSVImporter importer = new CSVImporter() {
      @Override
      protected void handleLine(String[] items) throws Exception {
        long count = Long.parseLong(items[1]);
        if (count >= minCount) {
          increment(items[0], Long.parseLong(items[1]));
        }
      }
    };
    importer.handleFile(file, 2);
  }

  public void read(File file) throws Exception {
    read(file, 1);
  }

}
