package co.com.btgpactual.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Response payload for subscription data returned by the API.
 */
@Data
@Builder
public class SubscriptionResponse {

    private String id;
    private String clientId;
    private String fundId;
    private String fundName;
    private BigDecimal montoVinculado;
    private Instant fechaApertura;
}
