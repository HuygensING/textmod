package nl.knaw.huygens.textmod.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import nl.knaw.huygens.textmod.api.XmlDocuments;
import nl.knaw.huygens.textmod.cocit.CocitationAnalyzer;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
  public Response getKeywords(XmlDocuments documents) {
    try {
      return Response.ok(analyzer.analyze(documents))
                     .build();
    } catch (Exception e) {
      return Response.serverError()
                     .build();
    }
  }

}
