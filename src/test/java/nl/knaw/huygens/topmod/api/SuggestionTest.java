package nl.knaw.huygens.topmod.api;

import nl.knaw.huygens.topmod.core.WeightedTerm;
import org.junit.Test;

public class SuggestionTest {

  @Test(expected = UnsupportedOperationException.class)
  public void testTermListIsImmutable() {
    new Suggestion(new WeightedTerm("a", 1.0))
      .getTerms().add(new WeightedTerm("this cannot be added", 1.1));
  }

}
