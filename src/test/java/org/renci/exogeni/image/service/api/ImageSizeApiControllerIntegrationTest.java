package org.renci.exogeni.image.service.api;


import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ImageSizeApiControllerIntegrationTest {

    @Autowired
    private ImageSizeApi api;

    @Test
    public void imageSizeGetTest() throws Exception {
        String project = "project_example";
        String userName = "userName_example";
        String password = "password_example";
        String imageId = "imageId_example";
        ResponseEntity<String> responseEntity = api.imageSizeGet(project, userName, password, imageId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

}
