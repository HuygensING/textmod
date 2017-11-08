package nl.knaw.huygens.textmod.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

public class XmlDocument {

  @ApiModelProperty(value = "identifier of document", required = true)
  @JsonProperty("id")
  public String id;

  @ApiModelProperty(value = "text of document", required = true)
  @JsonProperty("xml")
  public String xml;

}
