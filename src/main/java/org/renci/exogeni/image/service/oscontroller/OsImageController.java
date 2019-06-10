package org.renci.exogeni.image.service.oscontroller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.util.*;

public class OsImageController {
    private String region;
    private String authUrl;
    private String project;
    private String user;
    private String password;
    private String imageEndpoint;
    private String imageUrl = "/v2/images";
    private final RestTemplate rest = new RestTemplate();
    private final String tokenUrl = "/auth/tokens";
    private final static String X_Subject_Token = "X-Subject-Token";
    private static final Logger LOGGER = Logger.getLogger(OsImageController.class.getName());
    private static final String AUTH_DOCUMENT = "{\n" +
            "     \"auth\": {\n" +
            "         \"identity\": {\n" +
            "             \"methods\": [\"password\"],\n" +
            "             \"password\": {\n" +
            "                 \"user\": {\n" +
            "                     \"domain\": {\n" +
            "                         \"name\": \"default\"\n" +
            "                         },\n" +
            "                     \"name\": \"%s\",\n" +
            "                     \"password\": \"%s\"\n" +
            "                 }\n" +
            "             }\n" +
            "         },\n" +
            "         \"scope\": {\n" +
            "             \"project\": {\n" +
            "                 \"domain\": {\n" +
            "                     \"name\": \"default\"\n" +
            "                 },\n" +
            "                 \"name\":  \"%s\"\n" +
            "             }\n" +
            "         }\n" +
            "     }\n" +
            " }";

    public class ImageDetails {
        private InputStreamResource inputStreamResource;
        private Integer length;
        private String md5Checksum;

        public ImageDetails(InputStreamResource inputStreamResource, Integer length, String md5Checksum) {
            this.inputStreamResource = inputStreamResource;
            this.length = length;
            this.md5Checksum = md5Checksum;
        }
        public InputStreamResource getInputStreamResource() { return inputStreamResource; }
        public Integer getLength() { return length; }
        public String getMd5Checksum() { return md5Checksum; }
    }


    /*
     * @brief constructor
     *
     * @parm username - chameleon user name
     * @param password - chameleon user password
     * @param project - chameleon project Name
     */
    public OsImageController(String project , String user, String password) {
        this.authUrl = ImageConfig.getInstance().getOsAuthUrl();
        this.region = ImageConfig.getInstance().getOsRegion();
        this.project = project;
        this.user = user;
        this.password = password;
        imageEndpoint = null;
    }



    /*
     * @brief function to generate auth tokens to be used for openstack rest apis for reservations
     *
     * @param region - chameleon region
     *
     * @return token id
     *
     * @throws exception in case of error
     */
    private String auth(String region) throws Exception {
        try {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            HttpEntity<String> requestEntity = new HttpEntity<>(String.format(AUTH_DOCUMENT, user,
                    password, project), headers);

            ResponseEntity<Map> result = rest.exchange(authUrl + tokenUrl, HttpMethod.POST, requestEntity, Map.class);
            LOGGER.debug("Auth Token Post Response Status Code=" + result.getStatusCode());

            if (result.getStatusCode() == HttpStatus.OK ||
                    result.getStatusCode() == HttpStatus.ACCEPTED ||
                    result.getStatusCode() == HttpStatus.CREATED) {

                HttpHeaders resultHeaders = result.getHeaders();
                String token = resultHeaders.get(X_Subject_Token).get(0);
                Map<String, Object> tokenObject = (Map<String, Object>) result.getBody().get("token");

                if (tokenObject == null) {
                    throw new ImageServiceException("Failed to get token");
                }

                List<Map<String, Object>> catalog = (List<Map<String, Object>>) tokenObject.get("catalog");

                if (catalog == null) {
                    throw new ImageServiceException("Failed to get catalog");
                }

                Map<String, Object> imageEndPoint = null;
                for (Map<String, Object> endpoint : catalog) {
                    String name = (String) endpoint.get("name");
                    if (name.compareToIgnoreCase("glance") == 0) {

                        LOGGER.debug("endpoint=" + endpoint);
                        imageEndPoint = endpoint;
                        break;
                    }
                }
                List<Map<String, Object>> endPoints = (List<Map<String, Object>>) imageEndPoint.get("endpoints");
                if (endPoints == null) {
                    throw new ImageServiceException("Failed to get endPoints");
                }
                for (Map<String, Object> endpoint : endPoints) {
                    String endPointRegion = (String) endpoint.get("region");
                    String endPointInterface = (String) endpoint.get("interface");
                    if (endPointRegion.compareToIgnoreCase(region) == 0 &&
                            endPointInterface.compareToIgnoreCase("public") == 0) {
                        LOGGER.debug(endpoint);
                        imageEndpoint = (String) endpoint.get("url");
                        LOGGER.debug("Image Url = " + imageEndpoint);
                        break;
                    }
                }
                LOGGER.debug("Token = " + token);
                return token;
            }
        }
        catch (HttpClientErrorException e) {
            LOGGER.error("HTTP exception occurred e=" + e);
            LOGGER.error("HTTP Error response = " + e.getResponseBodyAsString());
            e.printStackTrace();
            throw new ImageServiceException(e.getResponseBodyAsString());
        }
        catch (Exception e) {
            LOGGER.error("Exception occured while getting tokens e=" + e);
            LOGGER.error("Message= " + e.getMessage());
            LOGGER.error("Message= " + e.getLocalizedMessage());

            e.printStackTrace();
            throw new ImageServiceException("failed to get token e=" + e.getMessage());

        }
        return null;
    }

    /*
     * @brief function to fetch a image with image id
     *
     * @param imageId - image id
     *
     * @return map containing lease object
     *
     * @throws exception in case of error
     */
    public ImageDetails getImage(String imageId) throws Exception {

        if (region == null || imageId == null) {
            throw new ImageServiceException("Failed to construct image request; invalid input params");
        }
        try {
            String token = auth(region);
            if (token != null) {
                HttpHeaders headers = new HttpHeaders();
                System.out.println("Setting token " + token);
                headers.set("X-Auth-Token", token);
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));
                HttpEntity<?> requestEntity = new HttpEntity<>(headers);
                ResponseEntity<byte[]> result = rest.exchange(imageEndpoint + "/" + imageUrl + "/" + imageId + "/file", HttpMethod.GET,
                        requestEntity, byte[].class);
                LOGGER.debug("Get Image Response Status Code=" + result.getStatusCode());

                if (result.getStatusCode() == HttpStatus.OK) {
                    LOGGER.debug("Successfully retrieved images");
                    LOGGER.debug("Body=" + result.getBody().length);
                    LOGGER.debug("Content-MD5=" + result.getHeaders().get("Content-MD5"));
                    String checksum = null;
                    if(result.getHeaders().get("Content-MD5").size() != 0) {
                        checksum = result.getHeaders().get("Content-MD5").get(0);
                    }
                    byte[] byteArr = result.getBody();
                    InputStreamResource inputStream = new InputStreamResource(new ByteArrayInputStream(byteArr));

                    return new ImageDetails(inputStream, result.getBody().length, checksum);
                } else {
                    throw new ImageServiceException("failed to get image " + result.getStatusCode());
                }
            }
            else {
                throw new ImageServiceException("failed to get token");
            }
        }
        catch (HttpClientErrorException e) {
            LOGGER.error("HTTP exception occurred e=" + e);
            LOGGER.error("HTTP Error response = " + e.getResponseBodyAsString());
            e.printStackTrace();
            throw new ImageServiceException(e.getResponseBodyAsString());
        }
        catch (Exception e) {
            LOGGER.error("Exception occured while retrieving image e=" + e);
            LOGGER.error("Message= " + e.getMessage());
            LOGGER.error("Message= " + e.getLocalizedMessage());
            e.printStackTrace();
            throw new ImageServiceException("failed to get image e=" + e.getMessage());
        }
    }

}
