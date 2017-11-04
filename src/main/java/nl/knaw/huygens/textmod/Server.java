package nl.knaw.huygens.textmod;

import io.dropwizard.Application;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.License;
import io.swagger.annotations.SwaggerDefinition;
import nl.knaw.huygens.textmod.keywords.KeywordModels;
import nl.knaw.huygens.textmod.resources.AboutResource;
import nl.knaw.huygens.textmod.resources.KeywordsResource;
import nl.knaw.huygens.textmod.resources.ModelsResource;
import nl.knaw.huygens.textmod.resources.SearchTermResource;
import nl.knaw.huygens.textmod.topics.TopicModels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

@SwaggerDefinition( //
    info = @Info( //
        description = "Text modeling service for Pergamon", //
        version = "1.0", //
        title = "TextMod: text modeling service", //
        termsOfService = "http://example.com/to-be-determined.html", //
        contact = @Contact( //
            name = "Developers", //
            email = "textmod@example.com", //
            url = "http://pergamon.huygens.knaw.nl" //
        ), //
        license = @License( //
            name = "GNU GENERAL PUBLIC LICENSE", //
            url = "https://www.gnu.org/licenses/licenses.en.html#GPL" //
        ) //
    ), //
    consumes = { MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON }, //
    schemes = { SwaggerDefinition.Scheme.HTTP, SwaggerDefinition.Scheme.HTTPS } //
)
public class Server extends Application<Config> {
  private static final Logger LOG = LoggerFactory.getLogger(Server.class);

  public static void main(String[] args) throws Exception {
    new Server().run(args);
  }

  @Override
  public String getName() {
    return "textmod";
  }

  @Override
  public void initialize(Bootstrap<Config> bootstrap) {
    bootstrap.addBundle(new MultiPartBundle());
    bootstrap.addBundle(new SwaggerBundle<Config>() {
      @Override
      protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(Config config) {
        return config.getSwaggerBundleConfig();
      }
    });
  }

  @Override
  public void run(Config config, Environment environment) throws Exception {
    LOG.debug("Running with config: {}", config);

    Properties buildProperties = extractBuildProperties().orElse(new Properties());
    File dataDirectory = validateDataDirectory(config.getDataDirectory());

    KeywordModels keywordModels = new KeywordModels(config).bootstrap();
    TopicModels topicModels = new TopicModels(config).bootstrap();

    JerseyEnvironment jersey = environment.jersey();
    jersey.register(new AboutResource(getName(), buildProperties));
    jersey.register(new KeywordsResource(keywordModels));
    jersey.register(new ModelsResource(topicModels, dataDirectory));
    jersey.register(new SearchTermResource(topicModels));
  }

  private Optional<Properties> extractBuildProperties() {
    final InputStream propertyStream = getClass().getClassLoader()
                                                 .getResourceAsStream("build.properties");
    if (propertyStream == null) {
      LOG.warn("Resource \"build.properties\" not found");
    } else {
      final Properties properties = new Properties();
      try {
        properties.load(propertyStream);
        LOG.debug("build.properties: {}", properties);
        return Optional.of(properties);
      } catch (IOException e) {
        LOG.warn("Unable to load build.properties: {}", e);
      }
    }
    return Optional.empty();
  }

  private File validateDataDirectory(File dataDirectory) throws IOException {
    if (dataDirectory.exists()) {
      LOG.info("Using existing data directory: {}", dataDirectory);
    } else if (dataDirectory.mkdirs()) {
      LOG.info("Created data directory: {}", dataDirectory);
    } else {
      throw new IOException("Failed to create data directory " + dataDirectory);
    }
    return dataDirectory;
  }

}
