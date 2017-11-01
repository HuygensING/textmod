package nl.knaw.huygens.textmod.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import nl.knaw.huygens.textmod.core.WeightedTerm;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Terms suggested by topic model.
 */
public class Suggestion {

  @JsonProperty("suggestions")
  private List<WeightedTerm> terms;

  public Suggestion() {
    this(Collections.emptyList());
  }

  public Suggestion(List<WeightedTerm> terms) {
    setTerms(terms);
  }

  public Suggestion(WeightedTerm... terms) {
    this(Arrays.asList(terms));
  }

  public List<WeightedTerm> getTerms() {
    return terms;
  }

  public void setTerms(List<WeightedTerm> terms) {
    this.terms = Collections.unmodifiableList(terms);
  }

}
