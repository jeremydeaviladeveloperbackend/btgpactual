package co.com.btgpactual.dto;

import co.com.btgpactual.model.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Response payload for transaction history entries.
 */
@Data
@Builder
public class TransactionResponse {

    private String id;
    private String clientId;
    private String fundId;
    private String fundName;
    private TransactionType tipo;
    private BigDecimal monto;
    private Instant fecha;
}
