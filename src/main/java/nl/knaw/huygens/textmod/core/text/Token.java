package nl.knaw.huygens.textmod.core.text;

import java.util.Comparator;

/**
 * Represents a token in a text. This may be a word, or an n-gram.
 */
public class Token {

  /** The text identifying the token. */
  private final String text;
  /** The number of occurrences of this token. */
  private long count;
  /** A (statistical) value associated with this token. */
  private double value;

  public Token(String text, long count, double value) {
    this.text = text;
    this.count = count;
    this.value = value;
  }

  public Token(String text) {
    this(text, 0, 0.0);
  }

  public String getText() {
    return text;
  }

  public long getCount() {
    return count;
  }

  public void increment(long value) {
    count += value;
  }

  public void increment() {
    increment(1);
  }

  public double getValue() {
    return value;
  }

  public void setValue(double value) {
    this.value = value;
  }

  /**
   * A comparator for sorting tokens by text, in natural order.
   */
  public static final Comparator<Token> TEXT_COMPARATOR = new Comparator<Token>() {
    @Override
    public int compare(Token token1, Token token2) {
      return token1.getText()
                   .compareTo(token2.getText());
    }
  };

  /**
   * A comparator for sorting tokens by count, from high to low.
   */
  public static final Comparator<Token> COUNT_COMPARATOR = new Comparator<Token>() {
    @Override
    public int compare(Token token1, Token token2) {
      return Long.compare(token2.getCount(), token1.getCount());
    }
  };

  /**
   * A comparator for sorting tokens by value, from high to low.
   */
  public static final Comparator<Token> VALUE_COMPARATOR = new Comparator<Token>() {
    @Override
    public int compare(Token token1, Token token2) {
      return Double.compare(token2.getValue(), token1.getValue());
    }
  };

}
