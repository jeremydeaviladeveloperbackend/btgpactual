package co.com.btgpactual.service;

import co.com.btgpactual.dto.ClientRequest;
import co.com.btgpactual.dto.ClientResponse;
import co.com.btgpactual.exception.BusinessException;
import co.com.btgpactual.model.Client;
import co.com.btgpactual.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Service for client management.
 * Handles client creation with initial balance and retrieval by identifier.
 */
@Service
@RequiredArgsConstructor
public class ClientService {

    private static final BigDecimal INITIAL_BALANCE = new BigDecimal("500000");

    private final ClientRepository clientRepository;

    /**
     * Creates a new client with initial balance and generates a unique clientId.
     *
     * @param request client data
     * @return the created client
     */
    public Mono<ClientResponse> createClient(ClientRequest request) {
        String clientId = UUID.randomUUID().toString();
        Client client = Client.builder()
                .clientId(clientId)
                .nombre(request.getNombre())
                .apellidos(request.getApellidos())
                .ciudad(request.getCiudad())
                .saldo(INITIAL_BALANCE)
                .preferenciaNotificacion(request.getPreferenciaNotificacion())
                .build();
        return clientRepository.save(client)
                .map(this::toResponse);
    }

    /**
     * Retrieves a client by its unique identifier.
     *
     * @param clientId unique client identifier
     * @return the client if found, empty Mono otherwise
     */
    public Mono<ClientResponse> getByClientId(String clientId) {
        return clientRepository.findByClientId(clientId)
                .map(this::toResponse);
    }

    private ClientResponse toResponse(Client client) {
        return ClientResponse.builder()
                .id(client.getId())
                .clientId(client.getClientId())
                .nombre(client.getNombre())
                .apellidos(client.getApellidos())
                .ciudad(client.getCiudad())
                .saldo(client.getSaldo())
                .preferenciaNotificacion(client.getPreferenciaNotificacion())
                .build();
    }
}
