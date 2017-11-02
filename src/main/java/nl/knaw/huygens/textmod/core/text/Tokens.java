package nl.knaw.huygens.textmod.core.text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A collection of tokens.
 */
public class Tokens implements Serializable {

  private static final long serialVersionUID = 1L;

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
    tokens.values()
          .stream()
          .sorted(comparator)
          .limit(maxSize)
          .forEach(handler::accept);
  }

  public void handleSorted(TokenHandler handler, Comparator<Token> comparator) {
    handleSorted(handler, comparator, Long.MAX_VALUE);
  }

  public void handle(TokenHandler handler) {
    tokens.values()
          .forEach(handler::accept);
  }

  @SuppressWarnings("unchecked")
  public void read(File file) throws IOException {
    try {
      ObjectInputStream stream = new ObjectInputStream(new FileInputStream(file));
      tokens = (Map<String, Token>) stream.readObject();
      stream.close();
    } catch (ClassNotFoundException e) {
      throw new IOException("Failed to read tokens", e);
    }
  }

  public void write(File file) throws IOException {
    ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file));
    stream.writeObject(tokens);
    stream.close();
  }

}
