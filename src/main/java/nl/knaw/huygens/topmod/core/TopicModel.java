package nl.knaw.huygens.topmod.core;

import java.util.List;

import com.google.common.collect.Lists;

public class TopicModel {

  public List<String> suggest(String query) {
    return Lists.newArrayList("lorem", "ipsum");
  }

}
