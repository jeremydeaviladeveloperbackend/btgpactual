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

/**
 * REST controller for client subscription operations.
 * Manages fund subscriptions, cancellations and transaction history.
 */
@RestController
@RequestMapping("/api/v1/clients/{clientId}")
@RequiredArgsConstructor
public class SubscriptionController {

    private final FundService fundService;

    /**
     * Subscribes a client to an investment fund.
     * Validates balance and prevents duplicate subscriptions.
     *
     * @param clientId  client identifier
     * @param request   fund to subscribe (fundId)
     * @return the created subscription
     */
    @PostMapping("/subscriptions")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<SubscriptionResponse> subscribe(@PathVariable String clientId,
                                                @Valid @RequestBody SubscribeRequest request) {
        return fundService.subscribe(clientId, request);
    }

    /**
     * Returns all active subscriptions for the client.
     *
     * @param clientId client identifier
     * @return flux of active subscriptions
     */
    @GetMapping("/subscriptions")
    public Flux<SubscriptionResponse> getActiveSubscriptions(@PathVariable String clientId) {
        return fundService.getActiveSubscriptions(clientId);
    }

    /**
     * Cancels a subscription and returns the linked amount to client balance.
     *
     * @param clientId       client identifier
     * @param subscriptionId subscription to cancel
     * @return the cancelled subscription details
     */
    @DeleteMapping("/subscriptions/{subscriptionId}")
    public Mono<SubscriptionResponse> cancelSubscription(@PathVariable String clientId,
                                                         @PathVariable String subscriptionId) {
        return fundService.cancelSubscription(clientId, subscriptionId);
    }

    /**
     * Retrieves the client's transaction history ordered by date descending.
     *
     * @param clientId client identifier
     * @return flux of transactions (openings and cancellations)
     */
    @GetMapping("/transactions")
    public Flux<TransactionResponse> getTransactionHistory(@PathVariable String clientId) {
        return fundService.getTransactionHistory(clientId);
    }
}
