package nl.knaw.huygens.topmod;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Environment;
import nl.knaw.huygens.topmod.core.TopicModel;
import nl.knaw.huygens.topmod.resources.AboutResource;
import nl.knaw.huygens.topmod.resources.KeywordSuggestResource;

public class Server extends Application<Server.Config> {
  private static final Logger LOG = LoggerFactory.getLogger(Server.class);

  public static void main(String[] args) throws Exception {
    new Server().run(args);
  }

  @Override
  public void run(Config config, Environment environment) throws Exception {
    LOG.debug("running with config: {}", config);

    Properties buildProperties = extractBuildProperties().orElse(new Properties());
    TopicModel model = new TopicModel();

    JerseyEnvironment jersey = environment.jersey();
    jersey.register(new AboutResource(buildProperties));
    jersey.register(new KeywordSuggestResource(model));
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

  static class Config extends Configuration {
  }
}
