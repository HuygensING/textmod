package nl.knaw.huygens.topmod;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

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

}
