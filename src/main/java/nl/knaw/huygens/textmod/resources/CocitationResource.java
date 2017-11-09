package nl.knaw.huygens.textmod.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import nl.knaw.huygens.textmod.api.CocitationResult;
import nl.knaw.huygens.textmod.api.XmlDocuments;
import nl.knaw.huygens.textmod.cocit.CocitationAnalyzer;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Api(CocitationResource.PATH)
@Path(CocitationResource.PATH)
@Produces(MediaType.APPLICATION_JSON)
public class CocitationResource {
  static final String PATH = "cocit";

  private final CocitationAnalyzer analyzer;

  public CocitationResource(CocitationAnalyzer analyzer) {
    this.analyzer = analyzer;
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Calculates cocitations")
  public CocitationResult getKeywords(XmlDocuments documents) {
    return analyzer.analyze(documents);
  }

}
