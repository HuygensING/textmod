package nl.knaw.huygens.topmod.core;

import java.util.Locale;

public class WeightedTerm {

  private final String text;
  private final float weight;

  public WeightedTerm(String text, float weight) {
    this.text = text;
    this.weight = weight;
  }

  public String getText() {
    return text;
  }

  public float getWeight() {
    return weight;
  }

  public String getSimilarity() {
    return String.format(Locale.US, "%5.3f", weight);
  }

}
