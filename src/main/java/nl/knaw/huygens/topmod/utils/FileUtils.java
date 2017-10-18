package nl.knaw.huygens.topmod.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class FileUtils {

  private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);

  public static Reader readerForFile(File file) throws IOException {
    return new InputStreamReader(new FileInputStream(file), "UTF-8");
  }

  public static <T extends Closeable> T closeQuietly(T closeable) {
    if (closeable != null) {
      try {
        closeable.close();
      } catch (IOException e) {
        LOG.error("Error closing {}: {}", closeable.getClass().getName(), e.getMessage());
      }
    }
    return null;
  }

  public static List<File> listZipFiles(File directory) {
    Objects.requireNonNull(directory);
    if (directory.isDirectory()) {
      return Arrays.asList(directory.listFiles(new ZipFilter()));
    } else {
      return Collections.emptyList();
    }
  }

  private static class ZipFilter implements FilenameFilter {
    @Override
    public boolean accept(File dir, String name) {
      return name.endsWith(".zip");
    }
  }

  private FileUtils() {
    throw new AssertionError("Non-instantiable class");
  }
}
