package nl.knaw.huygens.textmod.topics;

import com.google.common.collect.ImmutableList;
import nl.knaw.huygens.textmod.core.Language;
import nl.knaw.huygens.textmod.core.TermIndex;
import nl.knaw.huygens.textmod.core.WeightedTerm;
import nl.knaw.huygens.textmod.core.lucene.LuceneAnalyzer;
import nl.knaw.huygens.textmod.core.text.TextAnalyzer;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TopicModel {
  private static final Logger LOG = LoggerFactory.getLogger(TopicModel.class);

  private final File modelDirectory;

  public TopicModel(File modelDirectory) {
    this.modelDirectory = modelDirectory;
  }

  // ---------------------------------------------------------------------------

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

  /**
   * Returns the languages that are currently dealt with in topic modelling.
   */
  public List<Language> getAnalyzedLanguages() {
    return ImmutableList.of(Language.DUTCH, Language.FRENCH, Language.LATIN);
  }

  // ---------------------------------------------------------------------------

  public List<WeightedTerm> suggest(String query, int numTerms) {
    List<String> queryTerms = TextAnalyzer.parse(new LuceneAnalyzer(), query);
    List<String> normQueryTerms = normalizeTerms(queryTerms);
    // ensure sufficient number of suggestions, because of reduction
    List<WeightedTerm> normSuggestions = suggest(normQueryTerms, numTerms + queryTerms.size());
    List<WeightedTerm> suggestions = denormalizeWeightedTerms(normSuggestions);
    return reduce(suggestions, queryTerms, numTerms);
  }

  /**
   * Eliminate specified query terms from suggestions.
   */
  private List<WeightedTerm> reduce(List<WeightedTerm> suggestions, List<String> queryTerms, int numTerms) {
    return suggestions.stream()
                      .filter(suggestion -> !queryTerms.contains(suggestion.getText()))
                      .sorted() // by weight
                      .limit(numTerms)
                      .collect(Collectors.toList());
  }

  // -- semantic vectors -------------------------------------------------------

  private List<WeightedTerm> suggest(List<String> queryTerms, int numTerms) {
    Optional<List<WeightedTerm>> results = Optional.empty();
    if (!queryTerms.isEmpty() && numTerms > 0 && getTermVectors().exists()) {
      final FlagConfig flagConfig = FlagConfig.getFlagConfig(null);
      // CloseableVectorStore does not implement AutoCloseable, so collectAndClose instead of try-with-resources :-(
      results = openStore(flagConfig).flatMap(store -> collectAndClose(flagConfig, store, queryTerms, numTerms));
    }
    return results.orElse(Collections.emptyList());
  }

  private Optional<List<WeightedTerm>> collectAndClose(FlagConfig flagConfig, CloseableVectorStore store, List<String> queryTerms, int numTerms) {
    Optional<List<WeightedTerm>> results = getSearcher(flagConfig, store, queryTerms).map(searcher -> searcher.getNearestNeighbors(numTerms))
                                                                                     .map(neighbours -> neighbours.stream()
                                                                                                                  .map(this::toWeightedTerm)
                                                                                                                  .collect(Collectors.toList()));
    store.close();
    return results;
  }

  private WeightedTerm toWeightedTerm(SearchResult result) {
    String text = result.getObjectVector()
                        .getObject()
                        .toString();
    return new WeightedTerm(text, result.getScore());
  }

  private Optional<VectorSearcherCosine> getSearcher(FlagConfig flagConfig, CloseableVectorStore store, List<String> queryTerms) {
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

  // -- term index -------------------------------------------------------------

  private TermIndex getTermIndex() {
    return new TermIndex(getTermIndexDir());
  }

  /**
   * Adds terms from term files to the term index.
   */
  public void setupTermIndex() throws IOException {
    TermIndex termIndex = getTermIndex();
    for (Language language : getAnalyzedLanguages()) {
      File termFile = getTermFile(language);
      if (termFile.canRead()) {
        termIndex.addTermFile(termFile, language);
      }
    }
  }

  private List<String> normalizeTerms(List<String> terms) {
    List<String> normalized = new ArrayList<String>();
    TermIndex termIndex = getTermIndex();
    try {
      termIndex.openForReading();
      for (String term : terms) {
        normalized.add(termIndex.normalize(term));
      }
    } catch (IOException e) {
      return terms;
    } finally {
      termIndex.closeAfterReading();
    }
    return normalized;
  }

  public List<WeightedTerm> denormalizeWeightedTerms(List<WeightedTerm> terms) {
    List<WeightedTerm> results = new ArrayList<WeightedTerm>();
    TermIndex termIndex = getTermIndex();
    try {
      termIndex.openForReading();
      for (WeightedTerm term : terms) {
        List<WeightedTerm> denormalized = termIndex.denormalize(term.getText());
        if (denormalized.isEmpty()) {
          results.add(term);
        } else {
          double weight = term.getWeight();
          for (WeightedTerm t : denormalized) {
            results.add(new WeightedTerm(t.getText(), weight * t.getWeight()));
          }
        }
      }
    } catch (IOException e) {
      return terms;
    } finally {
      termIndex.closeAfterReading();
    }
    return results;
  }

}
