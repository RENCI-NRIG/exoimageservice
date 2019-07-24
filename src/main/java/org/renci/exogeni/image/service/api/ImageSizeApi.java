/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.3).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package org.renci.exogeni.image.service.api;

import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-07-23T14:40:10.501-04:00[America/New_York]")

@Api(value = "imageSize", description = "the imageSize API")
public interface ImageSizeApi {

    @ApiOperation(value = "returns image size given image id", nickname = "imageSizeGet", notes = "returns image size given image id ", response = String.class, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = String.class),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Internal Server Error"),
        @ApiResponse(code = 200, message = "Unexpected Error") })
    @RequestMapping(value = "/imageSize",
        produces = { "text/plain" }, 
        method = RequestMethod.GET)
    ResponseEntity<String> imageSizeGet(@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "project", required = true) String project,@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "userName", required = true) String userName,@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "password", required = true) String password,@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "imageId", required = true) String imageId);

}