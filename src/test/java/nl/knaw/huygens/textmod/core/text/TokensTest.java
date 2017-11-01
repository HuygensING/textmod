package nl.knaw.huygens.textmod.core.text;

import nl.knaw.huygens.textmod.core.text.Token;
import nl.knaw.huygens.textmod.core.text.TokenHandler;
import nl.knaw.huygens.textmod.core.text.Tokens;
import org.junit.Assert;
import org.junit.Test;

public class TokensTest {

  @Test
  public void testCountsForEmptyCollection() {
    Tokens tokens = new Tokens();
    Assert.assertEquals(0, tokens.getTotalTokenCount());
    Assert.assertEquals(0, tokens.getUniqueTokenCount());
  }

  @Test
  public void testCountsAfterIncrements() {
    Tokens tokens = new Tokens();
    tokens.increment(new String[] { "a", "b", "a" });
    Assert.assertEquals(3, tokens.getTotalTokenCount());
    Assert.assertEquals(2, tokens.getUniqueTokenCount());
  }

  @Test
  public void testSortByCount() {
    Tokens tokens = new Tokens();
    tokens.increment(new String[] { "a", "b", "b" });
    Concatenator concatenator = new Concatenator();
    tokens.handleSortedByCount(concatenator);
    Assert.assertEquals("ba", concatenator.toString());
  }

  @Test
  public void testSortbyValue() {
    Tokens tokens = new Tokens();
    tokens.increment(new String[] { "a", "a", "b" });
    tokens.get("a").setValue(1.0);
    tokens.get("b").setValue(2.0);
    Concatenator concatenator = new Concatenator();
    tokens.handleSortedByValue(concatenator);
    Assert.assertEquals("ba", concatenator.toString());
  }

  private static class Concatenator implements TokenHandler {
    private StringBuilder builder = new StringBuilder();

    @Override
    public boolean handle(Token token) {
      builder.append(token.getText());
      return true;
    }

    @Override
    public String toString() {
      return builder.toString();
    }
  }
}
