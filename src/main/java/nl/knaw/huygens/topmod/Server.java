package nl.knaw.huygens.topmod;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import nl.knaw.huygens.topmod.resources.KeywordSuggestResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    environment.jersey().register(new KeywordSuggestResource());
  }
}
