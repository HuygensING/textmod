package nl.knaw.huygens.topmod.core.text;

/**
 * Performs an action for a specified token.
 */
public interface TokenHandler {

  /**
   * Handles the specified token.
   * @param token the token to handle.
   * @return {@code true} if processing is to be continued, {@code false} otherwise.
   */
  boolean handle(Token token);

}
