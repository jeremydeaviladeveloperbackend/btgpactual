package co.com.btgpactual.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

/**
 * Request body for subscribing a client to a fund.
 */
@Data
@Builder
public class SubscribeRequest {

    @NotNull(message = "El ID del fondo es obligatorio")
    private String fundId;
}
