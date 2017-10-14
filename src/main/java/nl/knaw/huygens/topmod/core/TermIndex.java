package nl.knaw.huygens.topmod.core;

import com.google.common.collect.Lists;
import nl.knaw.huygens.topmod.core.lucene.LuceneAnalyzer;
import nl.knaw.huygens.topmod.core.lucene.LuceneUtils;
import nl.knaw.huygens.topmod.core.text.Language;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FloatField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
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

  public TermIndex(File indexDir) {
    this.indexDir = ensureIndexExists(indexDir);
  }

  protected Directory getDirectory() throws IOException {
    return FSDirectory.open(Paths.get(indexDir.getAbsolutePath()));
  }

  protected void closeDirectory() {
    directory = LuceneUtils.closeQuietly(directory);
  }

  private File ensureIndexExists(File indexDir) {
    if (indexDir != null && !indexDir.isDirectory()) {
      indexDir.mkdirs();
      IndexWriterConfig config = new IndexWriterConfig(new LuceneAnalyzer()) //
          .setOpenMode(OpenMode.CREATE_OR_APPEND);
      LOG.debug("IndexWriterConfig: {}", config);

      Directory directory = null;
      IndexWriter writer = null;
      try {
        directory = getDirectory();
        writer = new IndexWriter(directory, config);
      } catch (IOException e) {
        LOG.error("Error: {}", e.getMessage());
      } finally {
        LuceneUtils.closeQuietly(writer);
        closeDirectory();
      }
    }
    return indexDir;
  }

  public void addTermFile(File termFile, Language language) throws IOException {
    LOG.info("Add term file: {}", termFile.getName());

    IndexWriterConfig config = new IndexWriterConfig(new LuceneAnalyzer()) //
        .setOpenMode(OpenMode.CREATE_OR_APPEND);

    Directory directory = null;
    IndexWriter writer = null;
    try {
      directory = getDirectory();
      writer = new IndexWriter(directory, config);
      Term term = new Term("coningh" + language.getCode(), "koning", language.getCode(), 1);
      writer.addDocument(documentFor(term));
      LOG.info("Number of terms in index: {}", writer.numDocs());
    } catch (IOException e) {
      LOG.error("Error adding {}: {}", termFile.getName(), e.getMessage());
    } finally {
      LuceneUtils.closeQuietly(writer);
      closeDirectory();
    }
  }

  private Document documentFor(Term word) {
    Document document = new Document();
    document.add(new StringField(TERM_FIELD, word.getText(), Field.Store.YES));
    document.add(new StringField(NORM_FIELD, word.getNorm(), Field.Store.YES));
    document.add(new StringField(LANG_FIELD, word.getCode(), Field.Store.YES));
    document.add(new FloatField(FREQ_FIELD, word.getCount(), Field.Store.YES));
    return document;
  }

  // -- query ------------------------------------------------------------------

  private IndexReader reader;

  public void openForReading() throws IOException {
    directory = getDirectory();
    reader = DirectoryReader.open(directory);
  }

  public void closeAfterReading() {
    reader = LuceneUtils.closeQuietly(reader);
    closeDirectory();
  }

  /**
   * Returns a list with denormalized terms for a fully normalized term.
   */
  public List<String> denormalize(String term) throws IOException {
    IndexSearcher searcher = new IndexSearcher(reader);
    List<String> list = Lists.newArrayList();
    Query query = LuceneUtils.newTermQuery(NORM_FIELD, term);
    TopDocs docs = searcher.search(query, 10);
    for (ScoreDoc scoreDoc : docs.scoreDocs) {
      Document document = searcher.doc(scoreDoc.doc);
      list.add(document.get(TERM_FIELD));
    }
    return list;
  }

}
