package nl.knaw.huygens.textmod.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;

public class XmlDocuments {

  @ApiModelProperty(value = "identifier of document set", required = true)
  @JsonProperty("id")
  private String id;

  @ApiModelProperty(value = "set of documents", required = true)
  @JsonProperty("documents")
  private Set<XmlDocument> documents = Collections.emptySet();

  public XmlDocuments(String id) {
    this.id = id;
  }

  public XmlDocuments() {
    this("?");
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Set<XmlDocument> getDocuments() {
    return documents;
  }

  public void setDocuments(Set<XmlDocument> documents) {
    this.documents = documents;
  }

  public Stream<XmlDocument> stream() {
    return documents.stream();
  }

}
