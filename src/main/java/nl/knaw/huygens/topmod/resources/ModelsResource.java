package nl.knaw.huygens.topmod.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Api(ModelsResource.PATH)
@Path(ModelsResource.PATH)
@Produces(MediaType.APPLICATION_JSON)
public class ModelsResource {
  static final String PATH = "models";

  private static final Logger LOG = LoggerFactory.getLogger(ModelsResource.class);

  private final File dataDirectory;

  public ModelsResource(File dataDirectory) {
    this.dataDirectory = dataDirectory;
  }

  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @ApiOperation(value = "Uploads a zip-file containing topic model data")
  public void importModel(@FormDataParam("file") InputStream stream,
                          @FormDataParam("file") FormDataContentDisposition header) throws Exception {
    LOG.debug("Importing: {}", header.getFileName());
    unzipStream(stream, dataDirectory);
  }

  private void unzipStream(InputStream stream, File targetDir) throws IOException {
    File temp = null;
    try {
      temp = File.createTempFile("upload", "zip");
      FileUtils.copyToFile(stream, temp);
      unzipFile(temp, targetDir);
    } finally {
      if (temp != null) {
        temp.delete();
      }
    }
  }

  private void unzipFile(File file, File targetDir) throws IOException {
    java.nio.file.Path targetPath = targetDir.toPath();
    try (ZipFile zip = new ZipFile(file)) {
      Enumeration<? extends ZipEntry> entries = zip.entries();
      while (entries.hasMoreElements()) {
        ZipEntry entry = entries.nextElement();
        java.nio.file.Path path = targetPath.resolve(entry.getName());

        // Validate pathname to ensure it doesn't escape the temp directory.
        for (java.nio.file.Path part : path) {
          if ("..".equals(part.toString())) {
            throw new IOException("Pathname in zipfile may not contain ..");
          }
        }

        if (entry.isDirectory()) {
          if (!path.toFile().exists() && !path.toFile().mkdirs()) {
            throw new IOException("Could not create directory " + path);
          }
        } else {
          LOG.debug("Copying {}", path);
          FileUtils.copyToFile(zip.getInputStream(entry), path.toFile());
        }
      }
    }
  }

}
