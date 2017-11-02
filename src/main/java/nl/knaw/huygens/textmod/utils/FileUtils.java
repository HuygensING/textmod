package nl.knaw.huygens.textmod.utils;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileUtils {

  private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);

  public static Reader readerFor(File file) throws IOException {
    return new InputStreamReader(new FileInputStream(file), "UTF-8");
  }

  /**
   * Returns a {@code PrintWriter} for the specified file.
   */
  public static PrintWriter printWriterFor(File file) {
    try {
      return new PrintWriter(file, "UTF-8");
    } catch (IOException e) {
      throw new RuntimeException("Failed to create PrintWriter: " + e.getMessage());
    }
  }

  public static <T extends Closeable> T closeQuietly(T closeable) {
    if (closeable != null) {
      try {
        closeable.close();
      } catch (IOException e) {
        LOG.error("Error while closing: {}", e);
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

  /**
   * Unzips the specified file to the specified target directory.
   * Returns a list with names of the directories created in the target directory.
   */
  public static List<String> unzipFile(File file, File targetDir) throws IOException {
    List<String> dirNames = Lists.newArrayList();
    Path targetPath = targetDir.toPath();
    try (ZipFile zip = new ZipFile(file)) {
      Enumeration<? extends ZipEntry> entries = zip.entries();
      while (entries.hasMoreElements()) {
        ZipEntry entry = entries.nextElement();
        Path path = targetPath.resolve(entry.getName());

        // Validate pathname to ensure it doesn't escape the temp directory.
        for (Path part : path) {
          if ("..".equals(part.toString())) {
            throw new IOException("Pathname in zipfile may not contain ..");
          }
        }

        if (entry.isDirectory()) {
          Files.createDirectories(path);
          addDirName(dirNames, entry);
        } else {
          LOG.debug("Copying {}", path);
          copyFile(zip.getInputStream(entry), path);
        }
      }
    }
    return dirNames;
  }

  public static long copyFile(InputStream stream, Path target) throws IOException {
    return Files.copy(stream, target, StandardCopyOption.REPLACE_EXISTING);
  }

  private static void addDirName(List<String> names, ZipEntry entry) {
    Path path = Paths.get(entry.getName());
    if (path.getNameCount() == 1) {
      names.add(path.getName(0)
                    .toString());
    }
  }

  private FileUtils() {
    throw new AssertionError("Non-instantiable class");
  }
}
