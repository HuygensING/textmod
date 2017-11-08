package nl.knaw.huygens.textmod.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Set;

public class XmlDocuments {

  @ApiModelProperty(value = "identifier of document set", required = true)
  @JsonProperty("id")
  public String id;

  @ApiModelProperty(value = "set of documents", required = true)
  @JsonProperty("documents")
  public Set<XmlDocument> documents;

}
