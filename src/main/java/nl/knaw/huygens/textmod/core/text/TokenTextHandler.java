package nl.knaw.huygens.textmod.core.text;

/**
 * Performs actions for the text of a single token.
 */
public interface TokenTextHandler {

  void newSegment(String segmentId);

  void endSegment();

  void accept(String text);

}
