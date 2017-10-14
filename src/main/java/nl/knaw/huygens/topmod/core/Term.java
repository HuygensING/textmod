package nl.knaw.huygens.topmod.core;

public class Term {

  /** An unnormalized word. */
  private final String text;
  /** Its normalized equivalent. */
  private final String norm;
  /** Its language code. */
  private final String code;
  /** Its frequency in a corpus. */
  private final int count;

  public Term(String text, String norm, String code, int count) {
    this.text = text;
    this.norm = norm;
    this.code = code;
    this.count = count;
  }

  public String getText() {
    return text;
  }

  public String getNorm() {
    return norm;
  }

  public String getCode() {
    return code;
  }

  public int getCount() {
    return count;
  }

}
