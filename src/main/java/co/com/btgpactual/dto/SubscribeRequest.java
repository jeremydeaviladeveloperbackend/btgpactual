package co.com.btgpactual.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubscribeRequest {

    @NotNull(message = "El ID del fondo es obligatorio")
    private String fundId;
}
