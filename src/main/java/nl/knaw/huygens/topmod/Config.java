package nl.knaw.huygens.topmod;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

import java.io.File;

public class Config extends Configuration {

  private SwaggerBundleConfiguration swaggerBundleConfig;

  @JsonProperty("swagger")
  public SwaggerBundleConfiguration getSwaggerBundleConfig() {
    return swaggerBundleConfig;
  }

  @JsonProperty("swagger")
  public void setSwaggerBundleConfig(SwaggerBundleConfiguration swaggerBundleConfig) {
    this.swaggerBundleConfig = swaggerBundleConfig;
  }

  private String dataDirectoryName;

  @JsonProperty("dataDirectory")
  public String getDataDirectoryName() {
    return dataDirectoryName;
  }

  @JsonProperty("dataDirectory")
  public void setDataDirectoryName(String directoryName) {
    this.dataDirectoryName = directoryName;
  }

  @JsonIgnore
  public File getDataDirectory() {
    return new File(getDataDirectoryName());
  }

  @JsonIgnore
  public File getModelsDirectory() {
    return getDataDirectory();
  }

  @JsonIgnore
  public File getBootstrapDirectory() {
    return new File(getDataDirectory(), "bootstrap");
  }

  private String defaultModelName = "model";

  @JsonProperty("defaultModelName")
  public String getDefaultModelName() {
    return defaultModelName;
  }

  @JsonProperty("defaultModelName")
  public void setDefaultModelName(String defaultModelName) {
    this.defaultModelName = defaultModelName;
  }

}
