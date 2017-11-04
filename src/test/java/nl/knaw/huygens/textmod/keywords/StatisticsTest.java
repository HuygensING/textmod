package nl.knaw.huygens.textmod.keywords;

import org.junit.Assert;
import org.junit.Test;

import static nl.knaw.huygens.textmod.keywords.Statistics.logLikelihood;

public class StatisticsTest {

  @Test
  public void logLikelyhoodAcceptsZeroes() {
    Assert.assertEquals(0.0, logLikelihood(0, 0, 1, 1), 0.0);
    Assert.assertEquals(1.39, logLikelihood(0, 1, 1, 1), 0.005);
    Assert.assertEquals(1.39, logLikelihood(1, 0, 1, 1), 0.005);
  }

  @Test
  public void logLikelyhoodGivesZeroForEqualFrequencies() {
    Assert.assertEquals(0.0, logLikelihood(33, 33, 100, 100), 0.0);
    Assert.assertEquals(0.0, logLikelihood(1, 1, 1000, 1000), 0.0);
  }

  @Test
  public void logLikelyhoodReproducesResultsRayson() {
    Assert.assertEquals(40.19, logLikelihood(10, 15, 1000, 100), 0.005);
    Assert.assertEquals(95.68, logLikelihood(20, 34, 1000, 100), 0.005);
    Assert.assertEquals(105.40, logLikelihood(67, 54, 1000, 100), 0.005);
    Assert.assertEquals(208.70, logLikelihood(77, 88, 1000, 100), 0.005);
    Assert.assertEquals(190.23, logLikelihood(89, 87, 1000, 100), 0.005);
    Assert.assertEquals(118.96, logLikelihood(89, 65, 1000, 100), 0.005);
  }

}
