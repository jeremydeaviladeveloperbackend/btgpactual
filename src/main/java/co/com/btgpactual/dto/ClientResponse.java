package co.com.btgpactual.dto;

import co.com.btgpactual.model.NotificationPreference;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Response payload for client data returned by the API.
 */
@Data
@Builder
public class ClientResponse {

    private String id;
    private String clientId;
    private String nombre;
    private String apellidos;
    private String ciudad;
    private BigDecimal saldo;
    private NotificationPreference preferenciaNotificacion;
}
