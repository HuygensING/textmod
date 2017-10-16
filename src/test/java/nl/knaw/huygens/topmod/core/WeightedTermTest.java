package nl.knaw.huygens.topmod.core;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WeightedTermTest {

  @Test
  public void testConstruction() {
    List<WeightedTerm> list = Arrays.asList(new WeightedTerm("a", 1.0), new WeightedTerm("b", 1.0), new WeightedTerm("c", 2.0));
    Collections.sort(list);
    Assert.assertEquals(3, list.size());
    Assert.assertEquals("c", list.get(0).getText());
    Assert.assertEquals("a", list.get(1).getText());
    Assert.assertEquals("b", list.get(2).getText());
  }

}
