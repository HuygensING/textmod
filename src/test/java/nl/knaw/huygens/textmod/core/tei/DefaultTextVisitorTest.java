package nl.knaw.huygens.textmod.core.tei;

import org.junit.Assert;
import org.junit.Test;

public class DefaultTextVisitorTest {

  private String processText(String xml) {
    return Documents.visitXml(xml, new DefaultTextVisitor())
                    .getResult()
                    .replaceAll("\\s+", " ");
  }

  @Test
  public void testLinebreaks() {
    String xml = "<body>before<lb/>after</body>";
    Assert.assertEquals("before after", processText(xml));
  }

  @Test
  public void testPagebreaks() {
    Assert.assertEquals("before after", processText("<body>before<pb/>after</body>"));
    Assert.assertEquals("before after", processText("<body>before<pb break=\"anythingbutno\"/>after</body>"));
    Assert.assertEquals("beforeafter", processText("<body>before<pb break=\"no\"/>after</body>"));
  }

  @Test
  public void testParenthesesAndBrackets() {
    String xml = "<body>(a[b]) [c(d)]</body>";
    Assert.assertEquals("ab cd", processText(xml));
  }

  @Test
  public void testNumbers() {
    String xml = "<body>arabic:123 roman:<num type=\"roman\">MDC</num> other:<num type=\"other\">456</num></body>";
    Assert.assertEquals("arabic:123 roman:MDC other:456", processText(xml));
  }

  @Test
  public void testMath() {
    String xml = "<body><formula>x + y = z</formula></body>";
    Assert.assertEquals("x + y = z", processText(xml));
  }

}
