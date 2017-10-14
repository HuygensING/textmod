package nl.knaw.huygens.topmod.core;

import org.junit.Assert;
import org.junit.Test;

public class TermTest {

  @Test
  public void testQueryTerm() {
    Term term = new Term("text", "norm", "code", 42);
    Assert.assertEquals("text", term.getText());
    Assert.assertEquals("norm", term.getNorm());
    Assert.assertEquals("code", term.getCode());
    Assert.assertEquals(42, term.getCount());
  }

}
