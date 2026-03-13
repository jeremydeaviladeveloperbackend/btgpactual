package co.com.btgpactual.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * MongoDB document representing a client's subscription to a fund.
 * One client can only have one active subscription per fund (unique compound index).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "subscriptions")
@CompoundIndex(name = "client_fund_idx", def = "{'clientId': 1, 'fundId': 1}", unique = true)
public class Subscription {

    @Id
    private String id;

    private String clientId;
    private String fundId;
    private BigDecimal montoVinculado;
    private Instant fechaApertura;
}
