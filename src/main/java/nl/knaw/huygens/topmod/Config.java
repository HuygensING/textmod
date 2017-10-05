package nl.knaw.huygens.topmod;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;

public class Config extends Configuration {

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
