package nl.knaw.huygens.textmod.core.text;

import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.stream.Collectors;

public class TokensTest {

  private String concatenate(Tokens tokens, Comparator<Token> comparator) {
    return tokens.stream()
                 .sorted(comparator)
                 .map(Token::getText)
                 .collect(Collectors.joining());
  }

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
    Assert.assertEquals("ba", concatenate(tokens, Token.COUNT_COMPARATOR));
  }

  @Test
  public void testSortbyValue() {
    Tokens tokens = new Tokens();
    tokens.increment(new String[] { "a", "a", "b" });
    tokens.get("a")
          .setValue(1.0);
    tokens.get("b")
          .setValue(2.0);
    Assert.assertEquals("ba", concatenate(tokens, Token.VALUE_COMPARATOR));
  }

}
