package nl.knaw.huygens.textmod.api;

import java.util.Collections;
import java.util.Set;

public class CocitationResult {

  private Cocitations overall;

  private Set<Cocitations> detail;

  public CocitationResult() {
    overall = new Cocitations();
    detail = Collections.emptySet();
  }

  public Cocitations getOverall() {
    return overall;
  }

  public void setOverall(Cocitations overall) {
    this.overall = overall;
  }

  public Set<Cocitations> getDetail() {
    return detail;
  }

  public void setDetail(Set<Cocitations> detail) {
    this.detail = detail;
  }

}
