package nl.knaw.huygens.textmod.keywords;

public class KeywordException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public KeywordException(String message) {
    super(message);
  }

  public KeywordException(String format, Object... args) {
    super(String.format(format, args));
  }

}
