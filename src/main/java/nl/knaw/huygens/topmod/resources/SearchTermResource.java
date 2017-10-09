package nl.knaw.huygens.topmod.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import nl.knaw.huygens.topmod.api.Suggestion;
import nl.knaw.huygens.topmod.core.TopicModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Api(SearchTermResource.PATH)
@Path(SearchTermResource.PATH)
@Produces(MediaType.APPLICATION_JSON)
public class SearchTermResource {
  static final String PATH = "suggest";

  private static final Logger LOG = LoggerFactory.getLogger(SearchTermResource.class);

  private final TopicModel topicModel;

  public SearchTermResource(TopicModel model) {
    topicModel = model;
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Calculates search term suggestions")
  public Suggestion getSearchTermSuggestions(SuggestParams params) {
    LOG.debug("Getting suggestions for '{}'", params);
    List<String> terms = topicModel.suggest(params.query, params.model, params.maxTerms);
    return new Suggestion(terms);
  }

  static class SuggestParams {
    @ApiModelProperty(value = "terms entered by user to seed the suggestion", required = true)
    @JsonProperty
    String query;

    @ApiModelProperty(value = "topic model id to be used for the suggestions")
    @JsonProperty
    String model = "default";

    @ApiModelProperty(value = "max. number of suggestion terms to be returned")
    @JsonProperty
    int maxTerms = 10;

    @Override
    public String toString() {
      return MoreObjects.toStringHelper(this) //
          .add("query", query) //
          .add("model", model) //
          .add("maxTerms", maxTerms) //
          .toString();
    }
  }

}
