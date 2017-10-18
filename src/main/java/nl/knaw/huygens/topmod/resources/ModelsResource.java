package nl.knaw.huygens.topmod.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import nl.knaw.huygens.topmod.core.TopicModel;
import nl.knaw.huygens.topmod.utils.FileUtils;
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
  public void importModel(@FormDataParam("file") InputStream stream, @FormDataParam("file") FormDataContentDisposition header) throws Exception {
    LOG.debug("Importing: {}", header.getFileName());
    unzipStream(stream, dataDirectory);
    new TopicModel(new File(dataDirectory, "model")).setupTermIndex();
  }

  private void unzipStream(InputStream stream, File targetDir) throws IOException {
    File temp = null;
    try {
      temp = File.createTempFile("upload", "zip");
      FileUtils.copyFile(stream, temp.toPath());
      FileUtils.unzipFile(temp, targetDir);
    } finally {
      if (temp != null) {
        temp.delete();
      }
    }
  }

}
