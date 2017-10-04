package nl.knaw.huygens.topmod.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.knaw.huygens.topmod.api.Suggestion;

@Path("suggest")
@Produces(MediaType.APPLICATION_JSON)
public class KeywordSuggestResource {
  private static final Logger LOG = LoggerFactory.getLogger(KeywordSuggestResource.class);

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Suggestion getKeywordSuggestions(List<String> query) {
    LOG.debug("Getting keyword suggestions for: {}", query);
    return new Suggestion("lorem", "ipsum");
  }

}
