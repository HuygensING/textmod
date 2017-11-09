package nl.knaw.huygens.textmod.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

public class XmlDocument {

  @ApiModelProperty(value = "identifier of document", required = true)
  @JsonProperty("id")
  private String id;

  @ApiModelProperty(value = "text of document", required = true)
  @JsonProperty("xml")
  private String xml;

  public XmlDocument(String id, String xml) {
    this.id = id;
    this.xml = xml;
  }

  public XmlDocument() {
    this("?", "");
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getXml() {
    return xml;
  }

  public void setXml(String xml) {
    this.xml = xml;
  }

}
