package nl.knaw.huygens.textmod.keywords;

import nl.knaw.huygens.textmod.api.Keyword;
import nl.knaw.huygens.textmod.api.Keywords;

import java.io.File;
import java.util.Arrays;

public class KeywordModel {

  public KeywordModel(File modelDirectory) {

  }

  public Keywords determineKeywords(String xml, int n) {
    return getDefaultResult();
  }

  public Keywords getDefaultResult() {
    Keyword kw1 = new Keyword(0.9, Arrays.asList("de", "het", "een"));
    Keyword kw2 = new Keyword(0.8, Arrays.asList("le", "la"));
    return new Keywords(Arrays.asList(kw1, kw2));
  }

}
