package nl.knaw.huygens.textmod.core;

import nl.knaw.huygens.textmod.core.TopicModel;
import nl.knaw.huygens.textmod.core.text.Language;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class TopicModelTest {

  @Test
  public void testTermFilePath() {
    TopicModel model = new TopicModel(new File("/data"));
    File termFile = model.getTermFile(Language.FRENCH);
    Assert.assertEquals("/data/terms/terms-fr.txt", termFile.getAbsolutePath());
  }

  @Test
  public void testAnalyzedLanguages() {
    TopicModel model = new TopicModel(new File("/data"));
    List<Language> languages = model.getAnalyzedLanguages();
    Assert.assertTrue(languages.contains(Language.DUTCH));
    Assert.assertFalse(languages.contains(Language.GERMAN));
  }

}
