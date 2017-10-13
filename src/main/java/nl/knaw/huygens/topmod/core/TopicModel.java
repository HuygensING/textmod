package nl.knaw.huygens.topmod.core;

import com.google.common.collect.ImmutableList;
import nl.knaw.huygens.topmod.core.lucene.LuceneAnalyzer;
import nl.knaw.huygens.topmod.core.text.Language;
import nl.knaw.huygens.topmod.core.text.ListTokenTextHandler;
import nl.knaw.huygens.topmod.core.text.TextAnalyzer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pitt.search.semanticvectors.CloseableVectorStore;
import pitt.search.semanticvectors.FlagConfig;
import pitt.search.semanticvectors.SearchResult;
import pitt.search.semanticvectors.VectorSearcher.VectorSearcherCosine;
import pitt.search.semanticvectors.VectorStoreReader;
import pitt.search.semanticvectors.vectors.ZeroVectorException;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TopicModel {
  private static final Logger LOG = LoggerFactory.getLogger(TopicModel.class);

  // -- directories and files --------------------------------------------------

  private final File modelDirectory;

  public TopicModel(File modelDirectory) {
    this.modelDirectory = modelDirectory;
  }

  public File getDocVectors() {
    return new File(modelDirectory, "docvectors.bin");
  }

  public File getTermVectors() {
    return new File(modelDirectory, "termvectors.bin");
  }

  public File getTermDir() {
    return new File(modelDirectory, "terms");
  }

  public File getTermFile(Language language) {
    return new File(getTermDir(), String.format("terms-%s.txt", language.getCode()));
  }

  public File getTermIndexDir() {
    return new File(modelDirectory, "index");
  }

  // ---------------------------------------------------------------------------

  /**
   * Returns the languages that are currently dealt with in topic modelling.
   */
  public List<Language> getAnalyzedLanguages() {
    return ImmutableList.of(Language.DUTCH, Language.FRENCH, Language.LATIN);
  }

  // ---------------------------------------------------------------------------

  public List<WeightedTerm> suggest(String query, String model, int numTerms) {
    // model is currently ignored
    List<String> queryTerms = parseQuery(query);
    return suggest(queryTerms, numTerms);
  }

  private List<String> parseQuery(String query) {
    try {
      ListTokenTextHandler handler = new ListTokenTextHandler();
      TextAnalyzer.newInstance(new LuceneAnalyzer(), handler).analyze(query);
      return handler.getTokens();
    } catch (IOException e) {
      return Arrays.asList(query.toLowerCase().split("\\s+"));
    }
  }

  private List<WeightedTerm> suggest(List<String> queryTerms, int numTerms) {
    Optional<List<WeightedTerm>> results = Optional.empty();

    if (!queryTerms.isEmpty() && numTerms > 0 && getTermVectors().exists()) {
      final FlagConfig flagConfig = FlagConfig.getFlagConfig(null);

      // CloseableVectorStore does not implement AutoCloseable, so collectAndClose instead of try-with-resources :-(
      results = openStore(flagConfig).flatMap(store -> collectAndClose(flagConfig, store, queryTerms, numTerms));
    }

    return results.orElse(Collections.emptyList());
  }

  private Optional<List<WeightedTerm>> collectAndClose(FlagConfig flagConfig, CloseableVectorStore store,
                                                       List<String> queryTerms, int numTerms) {
    Optional<List<WeightedTerm>> results = getSearcher(flagConfig, store, queryTerms)
      .map(searcher -> searcher.getNearestNeighbors(numTerms))
      .map(neighbours -> neighbours.stream()
                                   .map(this::toWeightedTerm)
                                   .collect(Collectors.toList()));

    store.close();

    return results;
  }

  private WeightedTerm toWeightedTerm(SearchResult result) {
    return new WeightedTerm(result.getObjectVector().getObject().toString(), result.getScore());
  }

  private Optional<VectorSearcherCosine> getSearcher(FlagConfig flagConfig, CloseableVectorStore store,
                                                     List<String> queryTerms) {
    try {
      String[] queryArray = queryTerms.toArray(new String[0]);
      return Optional.of(new VectorSearcherCosine(store, store, null, flagConfig, queryArray));
    } catch (ZeroVectorException e) {
      LOG.warn("Unable to instantiate VectorSearcherCosine: {}", e);
      return Optional.empty();
    }
  }

  private Optional<CloseableVectorStore> openStore(FlagConfig flagConfig) {
    try {
      return Optional.of(VectorStoreReader.openVectorStore(getTermVectors().getPath(), flagConfig));
    } catch (IOException e) {
      LOG.warn("Unable to open VectorStore: {}", e);
      return Optional.empty();
    }
  }

}
