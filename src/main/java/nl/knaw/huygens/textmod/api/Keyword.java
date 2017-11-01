package nl.knaw.huygens.textmod.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

public class Keyword {

  @JsonProperty("weight")
  private double weight;

  @JsonProperty("terms")
  private List<String> terms;

  public Keyword(double weight, List<String> terms) {
    setWeight(weight);
    setTerms(terms);
  }

  public Keyword() {
    this(0.0, Collections.emptyList());
  }

  public double getWeight() {
    return weight;
  }

  public void setWeight(double weight) {
    this.weight = weight;
  }

  public List<String> getTerms() {
    return terms;
  }

  public void setTerms(List<String> terms) {
    this.terms = terms;
  }

}
