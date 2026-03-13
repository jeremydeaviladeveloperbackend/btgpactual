package co.com.btgpactual.dto;

import co.com.btgpactual.model.NotificationPreference;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

/**
 * Request body for creating a new client.
 */
@Data
@Builder
public class ClientRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidos;

    @NotBlank(message = "La ciudad es obligatoria")
    private String ciudad;

    @NotNull(message = "La preferencia de notificación es obligatoria")
    private NotificationPreference preferenciaNotificacion;
}
