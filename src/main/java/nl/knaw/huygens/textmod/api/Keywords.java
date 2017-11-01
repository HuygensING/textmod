package nl.knaw.huygens.textmod.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

public class Keywords {

  @JsonProperty("keywords")
  private List<Keyword> keywords;

  public Keywords(List<Keyword> keywords) {
    setKeywords(keywords);
  }

  public Keywords() {
    this(Collections.emptyList());
  }

  public List<Keyword> getKeywords() {
    return keywords;
  }

  public void setKeywords(List<Keyword> keywords) {
    this.keywords = Collections.unmodifiableList(keywords);
  }

}
