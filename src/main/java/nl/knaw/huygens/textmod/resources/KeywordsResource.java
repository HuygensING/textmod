package nl.knaw.huygens.textmod.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import nl.knaw.huygens.tei.Documents;
import nl.knaw.huygens.textmod.api.Keyword;
import nl.knaw.huygens.textmod.api.Keywords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import java.util.Arrays;

@Api(KeywordsResource.PATH)
@Path(KeywordsResource.PATH)
@Produces(MediaType.APPLICATION_JSON)
public class KeywordsResource {
  static final String PATH = "keywords";

  private static final Logger LOG = LoggerFactory.getLogger(KeywordsResource.class);

  public KeywordsResource() {
  }

  @POST
  @Consumes(MediaType.APPLICATION_XML)
  @ApiOperation(value = "Calculates keywords")
  public Keywords getKeywords(@ApiParam("Name of keyword model")
                              @QueryParam("model") @DefaultValue("default") String model,
                              @ApiParam("Maximum number of keywords")
                              @QueryParam("maxTerms") @DefaultValue("10") int maxTerms,
                              String xml) {
    LOG.debug("Getting keywords; model: {}, maxTerms: {}", model, maxTerms);
    Documents.newDocument(xml);
    return getDefaultResult();
  }

  private Keywords getDefaultResult() {
    Keyword kw1 = new Keyword(0.9, Arrays.asList("de", "het", "een"));
    Keyword kw2 = new Keyword(0.8, Arrays.asList("le", "la"));
    return new Keywords(Arrays.asList(kw1, kw2));
  }

}
