package nl.knaw.huygens.textmod.core.text;

/**
 * Token text handler that counts occurrences of tokens.
 */
public class CountHandler extends DefaultTokenTextHandler {

  private Tokens tokens;

  public CountHandler() {
    reset();
  }

  @Override
  public void accept(String text) {
    tokens.increment(text);
  }

  public void reset() {
    tokens = new Tokens();
  }

  public Tokens getTokens() {
    return tokens;
  }

}
