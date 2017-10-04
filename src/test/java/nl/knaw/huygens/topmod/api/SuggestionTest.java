package nl.knaw.huygens.topmod.api;

import org.junit.Test;

public class SuggestionTest {

  @Test(expected = UnsupportedOperationException.class)
  public void testTermListIsImmutable() {
    new Suggestion ("a", "b").getTerms().add("c");
  }

}
