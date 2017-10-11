package nl.knaw.huygens.topmod.core.lucene;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumerates Lucene filter options for processing text.
 */
public enum FilterOption {
  // language independent
  BASIC("basic", "basic preprocessing"), //
  DIACR("diacr", "diacritics removal"), //
  // language dependent
  SPELL("spell", "spelling normalization"), //
  STOP("stop", "stop word removal"), //
  STEM("stem", "stemming" //
  );

  private static Map<String, FilterOption> MAP = createMap();

  private static Map<String, FilterOption> createMap() {
    Map<String, FilterOption> map = new HashMap<String, FilterOption>();
    for (FilterOption option : FilterOption.values()) {
      map.put(option.getName(), option);
    }
    return map;
  }

  public static FilterOption getInstance(String name) {
    return MAP.get(name);
  }

  private final String name;
  private final String description;

  private FilterOption(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

}
