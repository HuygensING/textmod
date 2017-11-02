package nl.knaw.huygens.textmod.utils;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.File;
import java.io.Reader;

/**
 * Base class for handling CSV files.
 */
public abstract class CSVImporter {

  private static final char SEPARATOR_CHAR = ';';
  private static final char QUOTE_CHAR = '"';

  protected final char separatorChar;
  protected final char quoteChar;

  public CSVImporter(char separator, char quote) {
    separatorChar = separator;
    quoteChar = quote;
  }

  public CSVImporter() {
    this(SEPARATOR_CHAR, QUOTE_CHAR);
  }

  public void handleFile(File file, int minItemsPerLine) throws Exception {
    handleFile(FileUtils.readerFor(file), minItemsPerLine);
  }

  public void handleFile(Reader reader, int minItemsPerLine) throws Exception {
    initialize();
    CSVReader csvReader = null;
    try {
      CSVParser parser = new CSVParserBuilder().withSeparator(separatorChar).withQuoteChar(quoteChar).build();
      csvReader = new CSVReaderBuilder(reader).withCSVParser(parser).build();
      for (String[] line : csvReader) {
        if (acceptLine(line)) {
          validateLine(line, minItemsPerLine);
          handleLine(line);
        }
      }
      handleEndOfFile();
    } finally {
      if (csvReader != null) {
        csvReader.close();
      }
    }
  }

  /**
   * Performs initialization before handling of input.
   */
  protected void initialize() throws Exception {}

  /**
   * Performa actions after input handling of input.
   */
  protected void handleEndOfFile() throws Exception {};

  /**
   * Return {@code true} if the line must be handled, {@code false} otherwise.
   */
  protected boolean acceptLine(String[] items) {
    return (items.length != 0) && !items[0].trim().isEmpty() && !isComment(items);
  }

  private boolean isComment(String[] items) {
    return items[0].startsWith("--");
  }

  /**
   * Handles a parsed input line.
   */
  protected abstract void handleLine(String[] items) throws Exception;

  private void validateLine(String[] line, int minItems) {
    if (line.length < minItems) {
      throw new RuntimeException(String.format("# items < %d on line: %s...", minItems, line[0]));
    }
  }

}
