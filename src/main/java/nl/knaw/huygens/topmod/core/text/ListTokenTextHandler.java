package nl.knaw.huygens.topmod.core.text;

import java.util.ArrayList;
import java.util.List;

public class ListTokenTextHandler extends DefaultTokenTextHandler {
  private final List<String> tokens;

  public ListTokenTextHandler() {
    tokens = new ArrayList<String>();
  }

  @Override
  public void handle(String text) {
    tokens.add(text);
  }

  public List<String> getTokens() {
    return tokens;
  }

}
