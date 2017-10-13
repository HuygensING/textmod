package nl.knaw.huygens.topmod.core;

public class WeightedTerm {

  private final String text;
  private final double weight;

  public WeightedTerm(String text, double weight) {
    this.text = text;
    this.weight = weight;
  }

  public String getText() {
    return text;
  }

  public double getWeight() {
    return weight;
  }

}
