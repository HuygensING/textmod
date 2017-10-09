package nl.knaw.huygens.topmod.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Terms suggested by topic model.
 */
public class Suggestion {

  @JsonProperty("suggestions")
  private List<String> terms;

  public Suggestion() {
    this(Collections.emptyList());
  }

  public Suggestion(List<String> terms) {
    setTerms(terms);
  }

  public Suggestion(String... terms) {
    this(Arrays.asList(terms));
  }

  public List<String> getTerms() {
    return terms;
  }

  public void setTerms(List<String> terms) {
    this.terms = Collections.unmodifiableList(terms);
  }

}
