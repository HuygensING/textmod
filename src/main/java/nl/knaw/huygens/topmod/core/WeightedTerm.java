package nl.knaw.huygens.topmod.core;

public class WeightedTerm implements Comparable<WeightedTerm> {

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

  /**
   * Compares this {@code WeightedTerm} with the specified {@code WeightedTerm},
   * such that the term with the larger weight comes first when sorting.
   */
  @Override
  public int compareTo(WeightedTerm that) {
    return Double.compare(that.getWeight(), this.getWeight());
  }

}
