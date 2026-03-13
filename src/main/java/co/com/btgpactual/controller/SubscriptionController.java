package co.com.btgpactual.controller;

import co.com.btgpactual.dto.SubscribeRequest;
import co.com.btgpactual.dto.SubscriptionResponse;
import co.com.btgpactual.dto.TransactionResponse;
import co.com.btgpactual.service.FundService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/clients/{clientId}")
@RequiredArgsConstructor
public class SubscriptionController {

    private final FundService fundService;

    @PostMapping("/subscriptions")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<SubscriptionResponse> subscribe(@PathVariable String clientId,
                                                @Valid @RequestBody SubscribeRequest request) {
        return fundService.subscribe(clientId, request);
    }

    @GetMapping("/subscriptions")
    public Flux<SubscriptionResponse> getActiveSubscriptions(@PathVariable String clientId) {
        return fundService.getActiveSubscriptions(clientId);
    }

    @DeleteMapping("/subscriptions/{subscriptionId}")
    public Mono<SubscriptionResponse> cancelSubscription(@PathVariable String clientId,
                                                         @PathVariable String subscriptionId) {
        return fundService.cancelSubscription(clientId, subscriptionId);
    }

    @GetMapping("/transactions")
    public Flux<TransactionResponse> getTransactionHistory(@PathVariable String clientId) {
        return fundService.getTransactionHistory(clientId);
    }
}
