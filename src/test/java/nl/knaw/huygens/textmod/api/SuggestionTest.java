package nl.knaw.huygens.textmod.api;

import nl.knaw.huygens.textmod.api.Suggestion;
import nl.knaw.huygens.textmod.core.WeightedTerm;
import org.junit.Test;

public class SuggestionTest {

  @Test(expected = UnsupportedOperationException.class)
  public void testTermListIsImmutable() {
    new Suggestion(new WeightedTerm("a", 1.0))
      .getTerms().add(new WeightedTerm("this cannot be added", 1.1));
  }

}
