package nl.knaw.huygens.textmod.cocit;

import nl.knaw.huygens.textmod.api.Cocitation;
import nl.knaw.huygens.textmod.api.CocitationResult;
import nl.knaw.huygens.textmod.api.Cocitations;
import nl.knaw.huygens.textmod.api.XmlDocuments;

import java.util.Arrays;
import java.util.stream.Collectors;

public class CocitationAnalyzer {

  public CocitationAnalyzer() {}

  public CocitationResult analyze(XmlDocuments documents) {
    CocitationResult result = new CocitationResult();
    result.detail = documents.documents.stream()
                                       .map(d -> getCocitations(d.id, 1))
                                       .collect(Collectors.toSet());
    result.overall = getCocitations(documents.id, result.detail.size());
    return result;
  }

  private Cocitations getCocitations(String id, long count) {
    Cocitations cocitations = new Cocitations();
    cocitations.id = id;
    cocitations.cocitations = Arrays.asList( //
        new Cocitation(count, "Constantijn", "Christiaan"), //
        new Cocitation(count, "Constantijn", "Lodewijk") //
    );
    return cocitations;
  }

}
