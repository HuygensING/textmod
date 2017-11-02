package nl.knaw.huygens.textmod.core.text;

import java.io.Serializable;

/**
 * Represents a token in a text. This may be a word, or an n-gram.
 */
public class Token implements Serializable {

  private static final long serialVersionUID = 1L;

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

}
