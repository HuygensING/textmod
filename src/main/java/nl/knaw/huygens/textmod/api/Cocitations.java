package nl.knaw.huygens.textmod.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class Cocitations {

  @JsonProperty("id")
  private String id;

  @JsonProperty("cocitations")
  private List<Cocitation> cocitations;

  public Cocitations(String id, List<Cocitation> cocitations) {
    this.id = id;
    this.cocitations = cocitations;
  }

  public Cocitations() {
    this("?", Arrays.asList());
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setCocitations(List<Cocitation> cocitations) {
    this.cocitations = Objects.requireNonNull(cocitations);
  }

  public List<Cocitation> getCocitations() {
    return cocitations;
  }

  public Stream<Cocitation> stream() {
    return cocitations.stream();
  }

}
