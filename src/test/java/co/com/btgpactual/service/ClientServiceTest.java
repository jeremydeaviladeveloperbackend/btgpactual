package co.com.btgpactual.service;

import co.com.btgpactual.dto.ClientRequest;
import co.com.btgpactual.model.Client;
import co.com.btgpactual.model.NotificationPreference;
import co.com.btgpactual.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    private ClientRequest request;
    private Client savedClient;

    @BeforeEach
    void setUp() {
        request = ClientRequest.builder()
                .nombre("María")
                .apellidos("García")
                .ciudad("Medellín")
                .preferenciaNotificacion(NotificationPreference.EMAIL)
                .build();

        savedClient = Client.builder()
                .id("id-1")
                .clientId("uuid-1")
                .nombre(request.getNombre())
                .apellidos(request.getApellidos())
                .ciudad(request.getCiudad())
                .saldo(new BigDecimal("500000"))
                .preferenciaNotificacion(request.getPreferenciaNotificacion())
                .build();
    }

    @Test
    void createClient_devuelveClienteConSaldoInicial() {
        when(clientRepository.save(any(Client.class))).thenReturn(Mono.just(savedClient));

        StepVerifier.create(clientService.createClient(request))
                .expectNextMatches(r -> r.getClientId().equals("uuid-1") && r.getSaldo().compareTo(new BigDecimal("500000")) == 0)
                .verifyComplete();
    }
}
