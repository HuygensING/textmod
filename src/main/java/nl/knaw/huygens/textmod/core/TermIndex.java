package nl.knaw.huygens.textmod.core;

import com.google.common.collect.Lists;
import nl.knaw.huygens.textmod.core.lucene.LuceneAnalyzer;
import nl.knaw.huygens.textmod.core.lucene.LuceneUtils;
import nl.knaw.huygens.textmod.core.text.Language;
import nl.knaw.huygens.textmod.core.text.Token;
import nl.knaw.huygens.textmod.core.text.TokenHandler;
import nl.knaw.huygens.textmod.core.text.Tokens;
import nl.knaw.huygens.textmod.utils.CSVImporter;
import nl.knaw.huygens.textmod.utils.FileUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Paths;
import java.util.List;

public class TermIndex {

  private static final Logger LOG = LoggerFactory.getLogger(TermIndex.class);

  // generically preprocessed terms
  private static final String TERM_FIELD = "term";
  // fully normalized terms
  private static final String NORM_FIELD = "norm";
  // language
  private static final String LANG_FIELD = "lang";
  // frequency
  private static final String FREQ_FIELD = "freq";

  private final File indexDir;
  protected Directory directory;
  private IndexReader reader;

  public TermIndex(File indexDir) {
    this.indexDir = indexDir;
    ensureIndexExists(indexDir);
  }

  protected Directory getDirectory() throws IOException {
    return FSDirectory.open(Paths.get(indexDir.getAbsolutePath()));
  }

  protected void closeDirectory(Directory directory) {
    FileUtils.closeQuietly(directory);
  }

  private IndexWriter getIndexWriter(Directory directory) throws IOException {
    // Default mode is {@ link OpenMode.CREATE_OR_APPEND}.
    IndexWriterConfig config = new IndexWriterConfig(new LuceneAnalyzer());
    return new IndexWriter(directory, config);
  }

  private void ensureIndexExists(File indexDir) {
    if (indexDir != null && !indexDir.isDirectory()) {
      indexDir.mkdirs();
      Directory directory = null;
      IndexWriter writer = null;
      try {
        directory = getDirectory();
        writer = getIndexWriter(directory);
      } catch (IOException e) {
        LOG.error("Error: {}", e.getMessage());
      } finally {
        FileUtils.closeQuietly(writer);
        closeDirectory(directory);
      }
    }
  }

  public void addTermFile(File termFile, Language language) throws IOException {
    LOG.info("Add term file: {}", termFile.getName());
    addTermFile(FileUtils.readerFor(termFile), language);
  }

  public void addTermFile(Reader reader, Language language) throws IOException {
    Directory directory = null;
    IndexWriter writer = null;
    try {
      directory = getDirectory();
      writer = getIndexWriter(directory);
      new TermImporter(writer, language.getCode()).handleFile(reader, 3);
      LOG.info("Number of terms in index: {}", writer.numDocs());
    } catch (Exception e) {
      LOG.error("Error adding terms: {}", e.getMessage());
    } finally {
      FileUtils.closeQuietly(writer);
      closeDirectory(directory);
    }
  }

  private class TermImporter extends CSVImporter {
    private final IndexWriter writer;
    private final String langCode;

    public TermImporter(IndexWriter writer, String langCode) {
      this.writer = writer;
      this.langCode = langCode;
    }

    @Override
    protected void handleLine(String[] items) throws Exception {
      Document document = new Document();
      document.add(new StringField(LANG_FIELD, langCode, Field.Store.YES));
      document.add(new StringField(TERM_FIELD, items[0], Field.Store.YES));
      document.add(new StringField(NORM_FIELD, items[1], Field.Store.YES));
      document.add(new IntField(FREQ_FIELD, stringToInt(items[2]), Field.Store.YES));
      writer.addDocument(document);
    }

    private int stringToInt(String text) {
      try {
        return Integer.parseInt(text);
      } catch (NumberFormatException e) {
        return 1;
      }
    }
  }

  // -- query ------------------------------------------------------------------

  public void openForReading() throws IOException {
    directory = getDirectory();
    reader = DirectoryReader.open(directory);
  }

  public void closeAfterReading() {
    reader = FileUtils.closeQuietly(reader);
    closeDirectory(directory);
  }

  /**
   * Returns the fully normalized form of a term.
   * Note that the term is expected in basic normalized form
   * as produced by using {@code LuceneAnalyzer()}.
   */
  public String normalize(String term) throws IOException {
    IndexSearcher searcher = new IndexSearcher(reader);
    Query query = LuceneUtils.newTermQuery(TERM_FIELD, term);
    TopDocs docs = searcher.search(query, 1);
    for (ScoreDoc scoreDoc : docs.scoreDocs) {
      Document document = searcher.doc(scoreDoc.doc);
      return document.get(NORM_FIELD);
    }
    return term;
  }

  public List<WeightedTerm> denormalize(String term) throws IOException {
    IndexSearcher searcher = new IndexSearcher(reader);
    Query query = LuceneUtils.newTermQuery(NORM_FIELD, term);
    TopDocs docs = searcher.search(query, 100);

    Tokens tokens = new Tokens();
    for (ScoreDoc scoreDoc : docs.scoreDocs) {
      Document document = searcher.doc(scoreDoc.doc);
      int count = document.getField(FREQ_FIELD)
                          .numericValue()
                          .intValue();
      tokens.increment(document.get(TERM_FIELD), count);
    }
    return tokensToList(tokens);
  }

  private List<WeightedTerm> tokensToList(Tokens tokens) {
    final List<WeightedTerm> list = Lists.newArrayList();
    final double factor = 1.0 / tokens.getTotalTokenCount();
    tokens.handleSorted(new TokenHandler() {
      @Override
      public void accept(Token token) {
        String text = token.getText();
        double weight = factor * token.getCount();
        list.add(new WeightedTerm(text, weight));
      }
    }, Token.COUNT_COMPARATOR);
    return list;
  }

}
