package nl.knaw.huygens.textmod.core.text;

import org.junit.Assert;
import org.junit.Test;

public class TokenTest {

  @Test
  public void testSortingByText() {
    Assert.assertTrue(Token.TEXT_COMPARATOR.compare(new Token("a"), new Token("b")) < 0);
    Assert.assertTrue(Token.TEXT_COMPARATOR.compare(new Token("b"), new Token("b")) == 0);
    Assert.assertTrue(Token.TEXT_COMPARATOR.compare(new Token("c"), new Token("b")) > 0);
  }

  @Test
  public void testSortingByCount() {
    Assert.assertTrue(Token.COUNT_COMPARATOR.compare(new Token("a", 3, 1.0), new Token("b", 2, 2.0)) < 0);
    Assert.assertTrue(Token.COUNT_COMPARATOR.compare(new Token("a", 2, 1.0), new Token("b", 2, 2.0)) == 0);
    Assert.assertTrue(Token.COUNT_COMPARATOR.compare(new Token("b", 1, 2.0), new Token("a", 2, 1.0)) > 0);
  }

  @Test
  public void testSortingByValue() {
    Assert.assertTrue(Token.VALUE_COMPARATOR.compare(new Token("a", 1, 3.0), new Token("b", 2, 2.0)) < 0);
    Assert.assertTrue(Token.VALUE_COMPARATOR.compare(new Token("a", 1, 2.0), new Token("b", 2, 2.0)) == 0);
    Assert.assertTrue(Token.VALUE_COMPARATOR.compare(new Token("b", 2, 1.0), new Token("a", 1, 2.0)) > 0);
  }

}
