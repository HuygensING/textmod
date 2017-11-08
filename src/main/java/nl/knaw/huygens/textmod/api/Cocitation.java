package nl.knaw.huygens.textmod.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

public class Cocitation {

  @ApiModelProperty(value = "identifier of items", required = true)
  @JsonProperty("items")
  public String[] items;

  @ApiModelProperty(value = "number of references to both items", required = true)
  @JsonProperty("count")
  public long count;

  public Cocitation() {}

  public Cocitation(long count, String... items) {
    this.count = count;
    this.items = items;
  }

}
