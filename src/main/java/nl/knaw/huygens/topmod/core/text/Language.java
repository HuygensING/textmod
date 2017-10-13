package nl.knaw.huygens.topmod.core.text;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Enumerates a small number of languages, which are identified by their
 * IANA code (http://www.iana.org/assignments/language-subtag-registry).
 */
public enum Language {

  DUTCH("nl", "Dutch"), //
  ENGLISH("en", "English"), //
  FRENCH("fr", "French"), //
  GERMAN("de", "German"), //
  CLASSIC_GREEK("grc", "Greek"), //
  ITALIAN("it", "Italian"), //
  LATIN("la", "Latin"), //
  PORTUGUESE("pt", "Portuguese"), //
  SPANISH("es", "Spanish"), //
  UNKNOWN("?", "?");

  /** Regular languages, i.e., all except <code>UNKNOWN</code>. */
  public static final Set<Language> PROPER = EnumSet.complementOf(EnumSet.of(UNKNOWN));

  private static final Map<String, Language> LANGUAGE_MAP;

  static {
    LANGUAGE_MAP = new HashMap<String, Language>();
    for (Language language : Language.values()) {
      LANGUAGE_MAP.put(language.getCode(), language);
    }
  }

  /**
   * Returns the language corresponding with the specified code,
   * or <code>UNKNOWN</code> if no such code is defined.
   */
  public static Language getInstance(String code) {
    Language language = (code != null) ? LANGUAGE_MAP.get(code) : null;
    return (language != null) ? language : UNKNOWN;
  }

  /**
   * Returns the name of the language with the specified code.
   */
  public static String nameFor(String code) {
    return getInstance(code).getName();
  }

  public static boolean isProperLanguage(Language language) {
    return PROPER.contains(language);
  }

  // ---------------------------------------------------------------------------

  private final String code;
  private final String name;

  private Language(String code, String name) {
    this.code = code;
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return name;
  }

}
