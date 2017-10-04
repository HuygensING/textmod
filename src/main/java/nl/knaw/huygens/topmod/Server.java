package nl.knaw.huygens.topmod;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import nl.knaw.huygens.topmod.resources.AboutResource;
import nl.knaw.huygens.topmod.resources.KeywordSuggestResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

public class Server extends Application<Server.Config> {
  private static final Logger LOG = LoggerFactory.getLogger(Server.class);

  static class Config extends Configuration {
  }

  public static void main(String[] args) throws Exception {
    new Server().run(args);
  }

  @Override
  public void run(Config config, Environment environment) throws Exception {
    LOG.debug("running with config: {}", config);
    final Properties buildProperties = extractBuildProperties().orElse(new Properties());
    environment.jersey().register(new AboutResource(buildProperties));
    environment.jersey().register(new KeywordSuggestResource());
  }

  private Optional<Properties> extractBuildProperties() {
    final InputStream propertyStream = getClass().getClassLoader().getResourceAsStream("build.properties");
    if (propertyStream == null) {
      LOG.warn("Resource \"build.properties\" not found");
    }
    else {
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
}
