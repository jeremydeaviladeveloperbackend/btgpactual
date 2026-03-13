package co.com.btgpactual.controller;

import co.com.btgpactual.repository.FundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/funds")
@RequiredArgsConstructor
public class FundController {

    private final FundRepository fundRepository;

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
