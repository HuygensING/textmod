package nl.knaw.huygens.topmod.resources;

import nl.knaw.huygens.topmod.api.Suggestion;
import nl.knaw.huygens.topmod.core.TopicModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("suggest")
@Produces(MediaType.APPLICATION_JSON)
public class KeywordSuggestResource {
  private static final Logger LOG = LoggerFactory.getLogger(KeywordSuggestResource.class);

  private final TopicModel topicModel;

  public KeywordSuggestResource(TopicModel model) {
    topicModel = model;
  }

  @POST
  public Suggestion getKeywordSuggestions(String query) {
    LOG.debug("Getting suggestions for '{}'", query);
    List<String> terms = topicModel.suggest(query);
    return new Suggestion(terms);
  }

}
