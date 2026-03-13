package co.com.btgpactual.service;

import co.com.btgpactual.dto.SubscribeRequest;
import co.com.btgpactual.exception.BusinessException;
import co.com.btgpactual.exception.ResourceNotFoundException;
import co.com.btgpactual.model.Client;
import co.com.btgpactual.model.Fund;
import co.com.btgpactual.model.NotificationPreference;
import co.com.btgpactual.model.Subscription;
import co.com.btgpactual.model.Transaction;
import co.com.btgpactual.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FundServiceTest {

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private FundRepository fundRepository;
    @Mock
    private SubscriptionRepository subscriptionRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private FundService fundService;

    private Client client;
    private Fund fund;

    @BeforeEach
    void setUp() {
        client = Client.builder()
                .clientId("CLI-001")
                .nombre("Juan")
                .apellidos("Pérez")
                .saldo(new BigDecimal("500000"))
                .preferenciaNotificacion(NotificationPreference.EMAIL)
                .build();

        fund = Fund.builder()
                .fundId("1")
                .nombre("FPV_BTG_PACTUAL_RECAUDADORA")
                .montoMinimo(new BigDecimal("75000"))
                .categoria("FPV")
                .build();
    }

    @Test
    void subscribe_fallaCuandoClienteNoExiste() {
        when(clientRepository.findByClientId(anyString())).thenReturn(Mono.empty());
        var request = SubscribeRequest.builder().fundId("1").build();

        StepVerifier.create(fundService.subscribe("CLI-999", request))
                .expectError(ResourceNotFoundException.class)
                .verify();
    }

    @Test
    void subscribe_fallaCuandoNoHaySaldoSuficiente() {
        var poorClient = Client.builder().clientId("CLI-POOR").saldo(new BigDecimal("50000")).build();
        when(clientRepository.findByClientId(anyString())).thenReturn(Mono.just(poorClient));
        when(fundRepository.findByFundId(anyString())).thenReturn(Mono.just(fund));
        when(subscriptionRepository.existsByClientIdAndFundId(anyString(), anyString())).thenReturn(Mono.just(false));

        StepVerifier.create(fundService.subscribe("CLI-POOR", SubscribeRequest.builder().fundId("1").build()))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void subscribe_okYdevuelveRespuesta() {
        var savedSub = Subscription.builder()
                .id("sub-id")
                .clientId(client.getClientId())
                .fundId(fund.getFundId())
                .montoVinculado(fund.getMontoMinimo())
                .fechaApertura(Instant.now())
                .build();

        when(clientRepository.findByClientId(anyString())).thenReturn(Mono.just(client));
        when(fundRepository.findByFundId(anyString())).thenReturn(Mono.just(fund));
        when(subscriptionRepository.existsByClientIdAndFundId(anyString(), anyString())).thenReturn(Mono.just(false));
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(Mono.just(savedSub));
        when(clientRepository.save(any(Client.class))).thenReturn(Mono.just(client));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(Mono.just(Transaction.builder().build()));
        when(notificationService.notifySubscription(any(), any(), any())).thenReturn(Mono.empty());

        StepVerifier.create(fundService.subscribe("CLI-001", SubscribeRequest.builder().fundId("1").build()))
                .expectNextMatches(r -> r.getFundId().equals("1") && r.getFundName().equals(fund.getNombre()))
                .verifyComplete();
    }
}
