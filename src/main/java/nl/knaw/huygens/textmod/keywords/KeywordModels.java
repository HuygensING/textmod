package nl.knaw.huygens.textmod.keywords;

import nl.knaw.huygens.textmod.Config;
import nl.knaw.huygens.textmod.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class KeywordModels {
  private static final Logger LOG = LoggerFactory.getLogger(KeywordModels.class);

  public static final String DEFAULT_MODEL = "default";

  private final File bootstrapDirectory;
  private final File modelsDirectory;

  public KeywordModels(Config config) {
    bootstrapDirectory = new File(config.getBootstrapDirectory(), "keywords");
    modelsDirectory = new File(config.getDataDirectory(), "keywords");
  }

  private File modelDirectory(String name) {
    return new File(modelsDirectory, name);
  }

  public KeywordModel getDefaultModel() throws KeywordException {
    return new KeywordModel(DEFAULT_MODEL, modelDirectory(DEFAULT_MODEL));
  }

  public KeywordModel getModel(String name) throws KeywordException {
    return getDefaultModel();
  }

  public KeywordModels bootstrap() throws KeywordException {
    ensureDefaultModel();
    FileUtils.listZipFiles(bootstrapDirectory)
             .forEach(this::handleZipFile);
    return this;
  }

  private void ensureDefaultModel() throws KeywordException {
    File directory = modelDirectory(DEFAULT_MODEL);
    if (!directory.exists() && !directory.mkdirs()) {
      throw new KeywordException("Failed to create directory %s", directory);
    }
  }

  private void handleZipFile(File zipFile) {
    LOG.info("Handling: {}", zipFile.getAbsolutePath());
    try {
      final List<String> createdDirs = FileUtils.unzipFile(zipFile, modelsDirectory);
      LOG.trace("Unzipping {} created dirs: {}", zipFile.getAbsolutePath(), createdDirs);
      final boolean deleted = zipFile.delete();
      LOG.trace("Deleted {}: {}", zipFile.getAbsolutePath(), deleted);
    } catch (IOException e) {
      LOG.error(e.getMessage());
    }
  }

}
