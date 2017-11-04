package nl.knaw.huygens.textmod.core;

import org.junit.Assert;
import org.junit.Test;

public class LanguageTest {

  @Test
  public void testGetInstanceForUnknownLanguage() {
    Assert.assertEquals(Language.UNKNOWN, Language.getInstance(null));
    Assert.assertEquals(Language.UNKNOWN, Language.getInstance(""));
    Assert.assertEquals(Language.UNKNOWN, Language.getInstance("?"));
    Assert.assertEquals(Language.UNKNOWN, Language.getInstance("dummy"));
  }

  @Test
  public void testGetInstanceForKnownLanguage() {
    Assert.assertEquals(Language.DUTCH, Language.getInstance("nl"));
    Assert.assertEquals(Language.CLASSIC_GREEK, Language.getInstance("grc"));
    Assert.assertEquals(Language.LATIN, Language.getInstance("la"));
  }

  @Test
  public void testGetName() {
    Assert.assertEquals("Dutch", Language.DUTCH.getName());
    Assert.assertEquals("Latin", Language.LATIN.getName());
    Assert.assertEquals("?", Language.UNKNOWN.getName());
  }

  @Test
  public void testProperLanguages() {
    Assert.assertTrue(Language.PROPER.contains(Language.DUTCH));
    Assert.assertTrue(Language.PROPER.contains(Language.FRENCH));
    Assert.assertTrue(Language.PROPER.contains(Language.SPANISH));
    Assert.assertFalse(Language.PROPER.contains(Language.UNKNOWN));
  }

}
