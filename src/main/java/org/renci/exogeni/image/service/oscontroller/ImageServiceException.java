package org.renci.exogeni.image.service.oscontroller;

import org.springframework.http.HttpStatus;

/*
 * @brief class represents exception raised by imageservice
 *
 * @author kthare10
 */
public class ImageServiceException extends Exception{

    private HttpStatus status;
    public HttpStatus getStatus() { return status; }
    public ImageServiceException() {}

    public ImageServiceException(Throwable throwable) {
        super(throwable);
    }

    public ImageServiceException(HttpStatus s, String message) {
        super(message);
        status = s;
    }
    public ImageServiceException(String message) {
        super(message);
        status =  HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
