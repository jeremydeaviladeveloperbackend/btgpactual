package co.com.btgpactual.exception;

/**
 * Exception for business rule violations (e.g. insufficient balance, duplicate subscription).
 * Maps to HTTP 400 Bad Request.
 */
public class BusinessException extends RuntimeException {

    private final String code;

    public BusinessException(String message) {
        super(message);
        this.code = "BUSINESS_ERROR";
    }

    public BusinessException(String message, String code) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
