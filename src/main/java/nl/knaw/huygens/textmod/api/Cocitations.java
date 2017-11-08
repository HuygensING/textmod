package nl.knaw.huygens.textmod.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Cocitations {

  @JsonProperty("id")
  public String id;

  @JsonProperty("cocitations")
  public List<Cocitation> cocitations;

}
