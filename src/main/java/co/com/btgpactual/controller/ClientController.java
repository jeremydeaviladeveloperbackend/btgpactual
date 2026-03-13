package co.com.btgpactual.controller;

import co.com.btgpactual.dto.ClientRequest;
import co.com.btgpactual.dto.ClientResponse;
import co.com.btgpactual.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * REST controller for client management operations.
 * Exposes endpoints to create and retrieve clients.
 */
@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    /**
     * Creates a new client with initial balance.
     *
     * @param request client data (name, city, notification preference, etc.)
     * @return the created client with generated clientId
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ClientResponse> createClient(@Valid @RequestBody ClientRequest request) {
        return clientService.createClient(request);
    }

    /**
     * Retrieves a client by its identifier.
     *
     * @param clientId unique client identifier
     * @return the client if found
     */
    @GetMapping("/{clientId}")
    public Mono<ClientResponse> getClient(@PathVariable String clientId) {
        return clientService.getByClientId(clientId);
    }
}
