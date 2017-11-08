package nl.knaw.huygens.textmod.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import nl.knaw.huygens.textmod.api.Cocitation;
import nl.knaw.huygens.textmod.api.CocitationResult;
import nl.knaw.huygens.textmod.api.Cocitations;
import nl.knaw.huygens.textmod.api.XmlDocuments;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.util.Arrays;
import java.util.stream.Collectors;

@Api(CocitationResource.PATH)
@Path(CocitationResource.PATH)
@Produces(MediaType.APPLICATION_JSON)
public class CocitationResource {
  static final String PATH = "cocit";

  public CocitationResource() {}

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Calculates cocitations")
  public CocitationResult getKeywords(XmlDocuments documents) {
    return getCocitationResult(documents);
  }

  private CocitationResult getCocitationResult(XmlDocuments documents) {
    CocitationResult result = new CocitationResult();
    result.detail = documents.documents.stream().map(d -> getCocitations(d.id, 1)).collect(Collectors.toSet());
    result.overall = getCocitations(documents.id, result.detail.size());
    return result;
  }

  private Cocitations getCocitations(String id, long count) {
    Cocitations cocitations = new Cocitations();
    cocitations.id = id;
    cocitations.cocitations = Arrays.asList(new Cocitation(count, "Constantijn", "Christiaan"), new Cocitation(count, "Constantijn", "Lodewijk"));
    return cocitations;
  }

}
