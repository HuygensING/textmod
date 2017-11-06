package nl.knaw.huygens.textmod.keywords;

import com.google.common.base.Preconditions;

/**
 * Allows calculation of some statistical quantities.
 */
public final class Statistics {

  // cut-off for 99% confidence of significance
  public static final double THRESHOLD_99 = 6.63;

  /**
   * Calculates the log-likelihood statistic.
   */
  public static double logLikelihood(double f1, double f2, double n1, double n2) {
    Preconditions.checkArgument(f1 >= 0, "f1 >= 0 must hold");
    Preconditions.checkArgument(f2 >= 0, "f2 >= 0 must hold");
    Preconditions.checkArgument(n1 > 0, "n1 > 0 must hold");
    Preconditions.checkArgument(n2 > 0, "n2 > 0 must hold");
    double sum = 0;
    double term = (f1 + f2) / (n1 + n2);
    if (f1 > 0) {
      sum += f1 * Math.log(f1 / (n1 * term));
    }
    if (f2 > 0) {
      sum += f2 * Math.log(f2 / (n2 * term));
    }
    return 2 * sum;
  }

  private Statistics() {
    throw new AssertionError("Non-instantiable class");
  }
}
