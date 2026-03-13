package co.com.btgpactual.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

/**
 * MongoDB document representing an investment fund (FPV, FIC).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "funds")
public class Fund {

    @Id
    private String id;

    private String fundId;  // "1", "2", etc. - identificador de negocio
    private String nombre;
    private BigDecimal montoMinimo;
    private String categoria;  // FPV, FIC
}
