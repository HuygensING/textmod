package nl.knaw.huygens.topmod;

import io.dropwizard.Application;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import nl.knaw.huygens.topmod.core.TopicModel;
import nl.knaw.huygens.topmod.resources.AboutResource;
import nl.knaw.huygens.topmod.resources.KeywordSuggestResource;
import nl.knaw.huygens.topmod.resources.ModelsResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

public class Server extends Application<Config> {
  private static final String SERVICE_NAME = "TopMod";

  private static final Logger LOG = LoggerFactory.getLogger(Server.class);

  public static void main(String[] args) throws Exception {
    new Server().run(args);
  }

  @Override
  public void initialize(Bootstrap<Config> bootstrap) {
    bootstrap.addBundle(new MultiPartBundle());
  }

  @Override
  public void run(Config config, Environment environment) throws Exception {
    LOG.debug("Running with config: {}", config);

    Properties buildProperties = extractBuildProperties().orElse(new Properties());
    File dataDirectory = validateDataDirectory(config.getDataDirectoryName());
    TopicModel model = new TopicModel(new File(dataDirectory, "model"));

    JerseyEnvironment jersey = environment.jersey();
    jersey.register(new AboutResource(SERVICE_NAME, buildProperties));
    jersey.register(new KeywordSuggestResource(model));
    jersey.register(new ModelsResource(dataDirectory));
  }

  private Optional<Properties> extractBuildProperties() {
    final InputStream propertyStream = getClass().getClassLoader().getResourceAsStream("build.properties");
    if (propertyStream == null) {
      LOG.warn("Resource \"build.properties\" not found");
    } else {
      final Properties properties = new Properties();
      try {
        properties.load(propertyStream);

        if (LOG.isDebugEnabled()) {
          LOG.debug("build.properties: {}", properties);
        }

        return Optional.of(properties);
      } catch (IOException e) {
        LOG.warn("Unable to load build.properties: {}", e);
      }
    }

    return Optional.empty();
  }

  private File validateDataDirectory(String directoryName) throws IOException {
    File dataDirectory = new File(directoryName);
    if (dataDirectory.exists()) {
      LOG.info("Using existing data directory: {}", directoryName);
    } else if (dataDirectory.mkdirs()) {
      LOG.info("Created data directory: {}", directoryName);
    } else {
      throw new IOException("Failed to create data directory " + directoryName);
    }
    return dataDirectory;
  }
}
