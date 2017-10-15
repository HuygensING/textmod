package nl.knaw.huygens.topmod.utils;

import org.junit.Assert;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class CSVImporterTest {

  private static int MIN_ITEMS_PER_LINE = 2;

  private Reader input(String... lines) {
    // Concatenate lines, separated and terminated by newline
    String s = Arrays.asList(lines).stream().collect(Collectors.joining("\n", "", "\n"));
    return new StringReader(s);
  }

  private void testLineCount(int expectedLineCount, String... lines) throws Exception {
    final AtomicInteger count = new AtomicInteger();
    CSVImporter importer = new CSVImporter() {
      @Override
      protected void handleLine(String[] items) throws Exception {
        count.incrementAndGet();
      }
    };
    importer.handleFile(input(lines), MIN_ITEMS_PER_LINE);
    Assert.assertEquals(expectedLineCount, count.get());
  }

  @Test
  public void testSkippingComments() throws Exception {
    testLineCount(2, "--", "a;b", "--", "a;b", "--");
  }

  @Test
  public void testSkippingEmptyLines() throws Exception {
    testLineCount(2, "", "a;b", "", "a;b", "");
  }

  @Test
  public void testSkippingBlankLines() throws Exception {
    testLineCount(2, " ", "a;b", " ", "a;b", " ");
  }

  @Test(expected = RuntimeException.class)
  public void testTooFewItemsOnLine() throws Exception {
    testLineCount(3, "a;b", "a", "a;b");
  }

  @Test
  public void testTooManyItemsOnLine() throws Exception {
    testLineCount(3, "a;b", "a;b;c", "a;b");
  }

}
