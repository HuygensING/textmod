package nl.knaw.huygens.topmod.core.lucene;

import java.util.EnumSet;

/**
 * Defines a number of filter option sets.
 */
public class FilterOptions {

  public static EnumSet<FilterOption> none() {
    return EnumSet.noneOf(FilterOption.class);
  }

  public static EnumSet<FilterOption> all() {
    return EnumSet.allOf(FilterOption.class);
  }

  /** Generic, i.e. language independent, filters. */
  public static EnumSet<FilterOption> generic() {
    return EnumSet.of(FilterOption.BASIC, FilterOption.DIACR);
  }

  private FilterOptions() {
    throw new AssertionError("Non-instantiable class");
  }

}
