package co.com.btgpactual.controller;

import co.com.btgpactual.dto.ClientRequest;
import co.com.btgpactual.dto.ClientResponse;
import co.com.btgpactual.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ClientResponse> createClient(@Valid @RequestBody ClientRequest request) {
        return clientService.createClient(request);
    }

    @GetMapping("/{clientId}")
    public Mono<ClientResponse> getClient(@PathVariable String clientId) {
        return clientService.getByClientId(clientId);
    }
}
