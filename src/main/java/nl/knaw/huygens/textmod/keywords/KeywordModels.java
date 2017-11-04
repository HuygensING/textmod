package nl.knaw.huygens.textmod.keywords;

import nl.knaw.huygens.textmod.Config;

import java.io.File;

public class KeywordModels {
  public static final String MODELS_DIR = "keywords";
  public static final String DEFAULT_MODEL = "default";

  private final File modelsDirectory;

  public KeywordModels(Config config) {
    modelsDirectory = new File(config.getDataDirectory(), MODELS_DIR);
  }

  private File modelDirectory(String name) {
    return new File(modelsDirectory, name);
  }

  public KeywordModel getDefaultModel() throws KeywordException {
    return new KeywordModel(modelDirectory(DEFAULT_MODEL));
  }

  public KeywordModel getModel(String name) throws KeywordException {
    return getDefaultModel();
  }

  public KeywordModels bootstrap() throws KeywordException {
    ensureDefaultModel();
    return this;
  }

  private void ensureDefaultModel() throws KeywordException {
    File directory = modelDirectory(DEFAULT_MODEL);
    if (!directory.exists() && !directory.mkdirs()) {
      throw new KeywordException("Failed to create directory %s", directory);
    }
  }

}
