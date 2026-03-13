package co.com.btgpactual.controller;

import co.com.btgpactual.repository.FundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;

/**
 * REST controller for fund listing.
 * Exposes available investment funds with their basic information.
 */
@RestController
@RequestMapping("/api/v1/funds")
@RequiredArgsConstructor
public class FundController {

    private final FundRepository fundRepository;

    /**
     * Lists all available investment funds.
     *
     * @return flux of funds with fundId, nombre, montoMinimo and categoria
     */
    @GetMapping
    public Flux<Map<String, Object>> listFunds() {
        return fundRepository.findAll()
                .map(fund -> Map.<String, Object>of(
                        "fundId", fund.getFundId(),
                        "nombre", fund.getNombre(),
                        "montoMinimo", fund.getMontoMinimo(),
                        "categoria", fund.getCategoria()
                ));
    }
}
