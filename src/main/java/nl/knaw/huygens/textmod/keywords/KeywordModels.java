package nl.knaw.huygens.textmod.keywords;

import nl.knaw.huygens.textmod.Config;

import java.io.File;

public class KeywordModels {

  private final File modelsDirectory;
  private final String defaultModelName;

  public KeywordModels(Config config) {
    modelsDirectory = new File(config.getDataDirectory(), "keywords");
    defaultModelName = "default-model";
  }

  public String getDefaultModelName() {
    return defaultModelName;
  }

  public KeywordModel getDefaultModel() {
    return new KeywordModel(new File(modelsDirectory, defaultModelName));
  }

  public KeywordModel getModel(String name) {
    return getDefaultModel();
  }

  public KeywordModels bootstrap() {
    return this;
  }

}
