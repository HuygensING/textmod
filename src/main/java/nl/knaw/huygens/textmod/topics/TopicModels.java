package nl.knaw.huygens.textmod.topics;

import nl.knaw.huygens.textmod.Config;
import nl.knaw.huygens.textmod.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TopicModels {

  private static final Logger LOG = LoggerFactory.getLogger(TopicModels.class);

  private final Config config;
  private final File modelsDirectory;
  private final String defaultModelName;

  public TopicModels(Config config) {
    this.config = config;
    modelsDirectory = config.getModelsDirectory();
    defaultModelName = config.getDefaultModelName();
  }

  public String getDefaultModelName() {
    return defaultModelName;
  }

  public TopicModel getDefaultModel() {
    return new TopicModel(new File(modelsDirectory, defaultModelName));
  }

  public TopicModels bootstrap() {
    LOG.info("Looking for zipped models in: {}", config.getBootstrapDirectory());
    FileUtils.listZipFiles(config.getBootstrapDirectory())
             .forEach(file -> handleZipFile(file, config.getDataDirectory()));
    return this;
  }

  private void handleZipFile(File zipFile, File targetDirectory) {
    LOG.info("Handling: {}", zipFile.getAbsolutePath());
    try {
      List<String> names = FileUtils.unzipFile(zipFile, targetDirectory);
      if (names.contains(defaultModelName)) {
        getDefaultModel().setupTermIndex();
      }
      zipFile.delete();
    } catch (IOException e) {
      LOG.error(e.getMessage());
    }
  }

}
