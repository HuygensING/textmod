package nl.knaw.huygens.topmod.core;

import nl.knaw.huygens.topmod.Config;

import java.io.File;

public class TopicModels {

  private final File modelsDirectory;
  private final String defaultModelName;

  public TopicModels(Config config) {
    modelsDirectory = config.getModelsDirectory();
    defaultModelName = config.getDefaultModelName();
  }

  public String getDefaultModelName() {
    return defaultModelName;
  }

  public TopicModel getDefaultModel() {
    return new TopicModel(new File(modelsDirectory, defaultModelName));
  }

}
