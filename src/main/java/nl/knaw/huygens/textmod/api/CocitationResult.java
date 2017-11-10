package nl.knaw.huygens.textmod.api;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class CocitationResult {

  private List<Cocitation> overall;

  private Set<Cocitations> detail;

  public CocitationResult() {
    overall = Collections.emptyList();
    detail = Collections.emptySet();
  }

  public List<Cocitation> getOverall() {
    return overall;
  }

  public void setOverall(List<Cocitation> overall) {
    this.overall = overall;
  }

  public Set<Cocitations> getDetail() {
    return detail;
  }

  public void setDetail(Set<Cocitations> detail) {
    this.detail = detail;
  }

}
