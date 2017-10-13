package nl.knaw.huygens.topmod.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.time.Instant;
import java.util.Properties;

@Path("about")
public class AboutResource {
  @JsonProperty
  public final String serviceName;

  @JsonProperty
  public final String startedAt;

  @JsonProperty
  public final Properties buildProperties;

  public AboutResource(String serviceName, Properties buildProperties) {
    this.serviceName = serviceName;
    this.startedAt = Instant.now().toString();
    this.buildProperties = buildProperties;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response get() {
    return Response.ok(this).build();
  }

}
