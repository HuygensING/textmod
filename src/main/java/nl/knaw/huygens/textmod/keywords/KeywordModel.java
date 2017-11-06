package nl.knaw.huygens.textmod.keywords;

import nl.knaw.huygens.textmod.api.Keyword;
import nl.knaw.huygens.textmod.api.Keywords;
import nl.knaw.huygens.textmod.core.lucene.LuceneAnalyzer;
import nl.knaw.huygens.textmod.core.tei.Documents;
import nl.knaw.huygens.textmod.core.tei.FilterTextVisitor;
import nl.knaw.huygens.textmod.core.text.CountHandler;
import nl.knaw.huygens.textmod.core.text.TextAnalyzer;
import nl.knaw.huygens.textmod.core.text.Token;
import nl.knaw.huygens.textmod.core.text.Tokens;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class KeywordModel {
  private static final Logger LOG = LoggerFactory.getLogger(KeywordModel.class);

  private static final long MIN_COUNT = 2;

  private final Tokens corpusTokens;
  private final long corpusTokenCount;

  public KeywordModel(String name, File modelDirectory) throws KeywordException {
    File file = new File(modelDirectory, "word-counts.csv");
    corpusTokens = readTokens(name, file);
    corpusTokenCount = corpusTokens.getTotalTokenCount();
    LOG.debug("Reference corpus size: {}", corpusTokenCount);
  }

  private Tokens readTokens(String name, File file) throws KeywordException {
    Tokens tokens = new Tokens();
    if (file.canRead()) {
      try {
        tokens.read(file, MIN_COUNT);
      } catch (Exception e) {
        throw new KeywordException(e.getMessage());
      }
    }
    LOG.debug("Corpus size for model {}: {}", name, tokens.getUniqueTokenCount());
    return tokens;
  }

  public Keywords determineKeywords(String xml, int n) {
    if (xml == null || xml.isEmpty() || corpusTokenCount == 0) {
      return new Keywords();
    } else {
      Tokens tokens = extractTokens(xml);
      final long tokenCount = tokens.getTotalTokenCount();
      final double tokenCountInv = 1.0 / tokenCount;

      final double corpusTokenCountInv = 1.0 / corpusTokenCount;

      tokens.stream()
            .filter(t -> t.getCount() >= MIN_COUNT)
            .forEach(t -> {
              long n1 = t.getCount();
              long n2 = corpusTokens.getCountFor(t.getText());
              if (n1 * tokenCountInv > n2 * corpusTokenCountInv) {
                t.setValue(Statistics.logLikelihood(n1, n2, tokenCount, corpusTokenCount));
              }
            });

      return new Keywords(select(tokens, n));
    }
  }

  private Tokens extractTokens(String xml) {
    CountHandler countHandler = new CountHandler();
    TextAnalyzer<CountHandler> analyzer = TextAnalyzer.newInstance(new LuceneAnalyzer(), countHandler);
    Documents.visitXml(xml, FilterTextVisitor.newInstance(analyzer));
    return countHandler.getTokens();
  }

  private List<Keyword> select(Tokens tokens, int n) {
    return tokens.stream()
                 .filter(t -> (t.getValue() > Statistics.THRESHOLD_99))
                 .sorted(Token.VALUE_COMPARATOR)
                 .limit(n)
                 .map(t -> new Keyword(t.getValue(), t.getText()))
                 .collect(Collectors.toList());
  }

}
