package nl.knaw.huygens.textmod.keywords;

import nl.knaw.huygens.textmod.api.Keyword;
import nl.knaw.huygens.textmod.api.Keywords;
import nl.knaw.huygens.textmod.core.text.Tokens;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;

public class KeywordModel {
  private static final Logger LOG = LoggerFactory.getLogger(KeywordModel.class);

  // cut-off for 99% confidence of significance
  static final double THRESHOLD = 6.63;

  private final Tokens corpusTokens;

  public KeywordModel(String name, File modelDirectory) throws KeywordException {
    File file = new File(modelDirectory, "word-counts.csv");
    corpusTokens = readTokens(name, file);
  }

  Tokens readTokens(String name, File file) throws KeywordException {
    Tokens tokens = new Tokens();
    if (file.canRead()) {
      try {
        tokens.read(file);
      } catch (Exception e) {
        throw new KeywordException(e.getMessage());
      }
    }
    LOG.debug("Corpus size for model {}: {}", name, tokens.getUniqueTokenCount());
    return tokens;
  }

  public Keywords determineKeywords(String xml, int n) {
    return getDefaultResult();
  }

  private Keywords getDefaultResult() {
    Keyword kw1 = new Keyword(0.9, Arrays.asList("de", "het", "een"));
    Keyword kw2 = new Keyword(0.8, Arrays.asList("le", "la"));
    return new Keywords(Arrays.asList(kw1, kw2));
  }

}
