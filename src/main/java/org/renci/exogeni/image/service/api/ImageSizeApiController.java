package org.renci.exogeni.image.service.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.renci.exogeni.image.service.oscontroller.ImageServiceException;
import org.renci.exogeni.image.service.oscontroller.OsImageController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-07-23T14:40:10.501-04:00[America/New_York]")

@Controller
public class ImageSizeApiController implements ImageSizeApi {

    private static final Logger log = LoggerFactory.getLogger(ImageSizeApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public ImageSizeApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<String> imageSizeGet(@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "project", required = true) String project,@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "userName", required = true) String userName,@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "password", required = true) String password,@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "imageId", required = true) String imageId) {
        String accept = request.getHeader("Accept");

        OsImageController osImageController = null;
        try {
            osImageController = new OsImageController(project, userName, password);

            String imageSize = osImageController.getImageDetails(imageId);
            ResponseEntity<String> responseEntity = new  ResponseEntity<String>(HttpStatus.OK);

            return ResponseEntity.ok().contentType(MediaType.parseMediaType("text/plain")).body(imageSize);
        }
        catch (ImageServiceException e) {
            log.error("Exception occurred e=" + e);
            e.printStackTrace();
            return new ResponseEntity<String>(e.getStatus());
        }
        catch (Exception e) {
            log.error("Exception occurred e=" + e);
            e.printStackTrace();
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
