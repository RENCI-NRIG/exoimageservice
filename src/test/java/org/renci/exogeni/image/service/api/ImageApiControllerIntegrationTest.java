package org.renci.exogeni.image.service.api;


import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ImageApiControllerIntegrationTest {

    @Autowired
    private ImageApi api;

    @Test
    public void imageGetTest() throws Exception {
        String project = "project_example";
        String userName = "userName_example";
        String password = "password_example";
        String imageId = "imageId_example";
        ResponseEntity<InputStreamResource> responseEntity = api.imageGet(project, userName, password, imageId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

}