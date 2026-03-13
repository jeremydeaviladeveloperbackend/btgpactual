package co.com.btgpactual.exception;

/**
 * Exception when a requested resource (client, fund, subscription) does not exist.
 * Maps to HTTP 404 Not Found.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
