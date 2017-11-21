package nl.knaw.huygens.textmod.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import nl.knaw.huygens.textmod.api.XmlDocument;
import nl.knaw.huygens.textmod.cocit.CocitationAnalyzer;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import java.util.Set;

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
  public Response calculateCocitations(@ApiParam("Format of results")
                                       @QueryParam("format") @DefaultValue("simple") String format,
                                       Set<XmlDocument> documents) {
    try {
      switch (format.toLowerCase()) {
      case "full":
        return Response.ok(analyzer.asFull(documents)).build();
      case "graph":
        return Response.ok(analyzer.asGraph(documents)).build();
      case "simple":
        return Response.ok(analyzer.asSimple(documents)).build();
      default:
        return Response.status(Status.BAD_REQUEST).build();
      }
    } catch (Exception e) {
      return Response.serverError().build();
    }
  }

}
