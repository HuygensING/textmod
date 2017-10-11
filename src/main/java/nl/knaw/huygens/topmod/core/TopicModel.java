package nl.knaw.huygens.topmod.core;

import nl.knaw.huygens.topmod.core.lucene.LuceneAnalyzer;
import nl.knaw.huygens.topmod.core.text.ListTokenTextHandler;
import nl.knaw.huygens.topmod.core.text.TextAnalyzer;
import pitt.search.semanticvectors.CloseableVectorStore;
import pitt.search.semanticvectors.FlagConfig;
import pitt.search.semanticvectors.SearchResult;
import pitt.search.semanticvectors.VectorSearcher;
import pitt.search.semanticvectors.VectorStoreReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TopicModel {

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

  public List<String> suggest(String query, String model, int numTerms) {
    // model is currently ignored
    List<String> queryTerms = parseQuery(query);
    return suggest(queryTerms, numTerms) //
        .stream() //
        .map(t -> String.format("%s (%s)", t.getText(), t.getSimilarity())) //
        .collect(Collectors.toList());
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
    List<WeightedTerm> terms = new ArrayList<>();
    if (!queryTerms.isEmpty() && numTerms > 0 && getTermVectors().exists()) {
      FlagConfig flagConfig = FlagConfig.getFlagConfig(null);
      CloseableVectorStore store = null;
      try {
        store = VectorStoreReader.openVectorStore(getTermVectors().getPath(), flagConfig);
        String[] queryArray = queryTerms.toArray(new String[0]);
        VectorSearcher searcher = new VectorSearcher.VectorSearcherCosine(store, store, null, flagConfig, queryArray);
        List<SearchResult> results = searcher.getNearestNeighbors(numTerms);
        for (int i = 0; i < numTerms; i++) {
          SearchResult result = results.get(i);
          String text = result.getObjectVector().getObject().toString();
          terms.add(new WeightedTerm(text, (float) result.getScore()));
        }
      } catch (Exception e) {
        if (store != null) {
          store.close();
        }
      }
    }
    return terms;
  }

}
