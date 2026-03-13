package co.com.btgpactual.controller;

import co.com.btgpactual.dto.SubscribeRequest;
import co.com.btgpactual.dto.SubscriptionResponse;
import co.com.btgpactual.service.FundService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebFluxTest(SubscriptionController.class)
class SubscriptionControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private FundService fundService;

    @Test
    void subscribe_retorna201() {
        when(fundService.subscribe(eq("CLI-001"), any(SubscribeRequest.class)))
                .thenReturn(Mono.just(SubscriptionResponse.builder()
                        .id("sub-1")
                        .clientId("CLI-001")
                        .fundId("1")
                        .fundName("FPV_BTG")
                        .montoVinculado(new BigDecimal("75000"))
                        .fechaApertura(Instant.now())
                        .build()));

        webTestClient.post()
                .uri("/api/v1/clients/CLI-001/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(SubscribeRequest.builder().fundId("1").build())
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.fundId").isEqualTo("1");
    }
}
