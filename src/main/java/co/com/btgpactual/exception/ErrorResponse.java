package co.com.btgpactual.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

/**
 * Standard error response body for API errors.
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private Instant timestamp;
    private String code;
    private String message;
    private String path;
    private List<FieldError> fieldErrors;

    /** Validation error for a specific field. */
    @Data
    @Builder
    public static class FieldError {
        private String field;
        private String message;
    }
}
