package nl.knaw.huygens.topmod.core;

import nl.knaw.huygens.topmod.Config;

import java.io.File;

public class TopicModels {

  public static final String DEFAULT_MODEL_NAME = "model";

  private final File modelsDirectory;

  public TopicModels(Config config) {
    modelsDirectory = config.getModelsDirectory();
  }

  public TopicModel getDefaultModel() {
    return new TopicModel(new File(modelsDirectory, DEFAULT_MODEL_NAME));
  }

}
