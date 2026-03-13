package co.com.btgpactual.service;

import co.com.btgpactual.dto.SubscribeRequest;
import co.com.btgpactual.dto.SubscriptionResponse;
import co.com.btgpactual.dto.TransactionResponse;
import co.com.btgpactual.exception.BusinessException;
import co.com.btgpactual.exception.ResourceNotFoundException;
import co.com.btgpactual.model.*;
import co.com.btgpactual.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FundService {

    private final ClientRepository clientRepository;
    private final FundRepository fundRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final TransactionRepository transactionRepository;
    private final NotificationService notificationService;

    public Mono<SubscriptionResponse> subscribe(String clientId, SubscribeRequest request) {
        return validateClientExists(clientId)
                .flatMap(client -> validateFundExists(request.getFundId())
                        .flatMap(fund -> validateNoExistingSubscription(clientId, request.getFundId())
                                .then(validateSufficientBalance(client, fund))
                                .flatMap(c -> processSubscription(c, fund))));
    }

    public Mono<SubscriptionResponse> cancelSubscription(String clientId, String subscriptionId) {
        return validateClientExists(clientId)
                .flatMap(client -> subscriptionRepository.findByIdAndClientId(subscriptionId, clientId)
                        .switchIfEmpty(Mono.error(new ResourceNotFoundException("Suscripción no encontrada")))
                        .flatMap(subscription -> fundRepository.findByFundId(subscription.getFundId())
                                .flatMap(fund -> processCancellation(client, subscription, fund))));
    }

    public Flux<SubscriptionResponse> getActiveSubscriptions(String clientId) {
        return validateClientExists(clientId)
                .thenMany(subscriptionRepository.findByClientId(clientId))
                .flatMap(sub -> fundRepository.findByFundId(sub.getFundId())
                        .map(fund -> toResponse(sub, fund.getFundId(), fund.getNombre()))
                        .defaultIfEmpty(toResponse(sub, sub.getFundId(), "N/A")));
    }

    public Flux<TransactionResponse> getTransactionHistory(String clientId) {
        return validateClientExists(clientId)
                .thenMany(transactionRepository.findByClientIdOrderByFechaDesc(clientId))
                .flatMap(tx -> fundRepository.findByFundId(tx.getFundId())
                        .map(fund -> TransactionResponse.builder()
                                .id(tx.getTransactionId())
                                .clientId(tx.getClientId())
                                .fundId(tx.getFundId())
                                .fundName(fund.getNombre())
                                .tipo(tx.getTipo())
                                .monto(tx.getMonto())
                                .fecha(tx.getFecha())
                                .build())
                        .defaultIfEmpty(TransactionResponse.builder()
                                .id(tx.getTransactionId())
                                .clientId(tx.getClientId())
                                .fundId(tx.getFundId())
                                .fundName("N/A")
                                .tipo(tx.getTipo())
                                .monto(tx.getMonto())
                                .fecha(tx.getFecha())
                                .build()));
    }

    private Mono<Client> validateClientExists(String clientId) {
        return clientRepository.findByClientId(clientId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cliente no encontrado: " + clientId)));
    }

    private Mono<Fund> validateFundExists(String fundId) {
        return fundRepository.findByFundId(fundId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Fondo no encontrado: " + fundId)));
    }

    private Mono<Void> validateNoExistingSubscription(String clientId, String fundId) {
        return subscriptionRepository.existsByClientIdAndFundId(clientId, fundId)
                .flatMap(exists -> exists
                        ? Mono.error(new BusinessException("Ya tiene una suscripción activa en este fondo"))
                        : Mono.empty());
    }

    private Mono<Client> validateSufficientBalance(Client client, Fund fund) {
        if (client.getSaldo().compareTo(fund.getMontoMinimo()) < 0) {
            return Mono.error(new BusinessException(
                    "No tiene saldo disponible para vincularse al fondo " + fund.getNombre()));
        }
        return Mono.just(client);
    }

    private Mono<SubscriptionResponse> processSubscription(Client client, Fund fund) {
        Subscription subscription = Subscription.builder()
                .clientId(client.getClientId())
                .fundId(fund.getFundId())
                .montoVinculado(fund.getMontoMinimo())
                .fechaApertura(Instant.now())
                .build();

        return subscriptionRepository.save(subscription)
                .flatMap(saved -> updateClientBalance(client.getClientId(), client.getSaldo().subtract(fund.getMontoMinimo()))
                        .then(saveTransaction(client.getClientId(), fund.getFundId(), saved.getId(),
                                TransactionType.APERTURA, fund.getMontoMinimo()))
                        .then(notificationService.notifySubscription(client, fund, saved))
                        .thenReturn(toResponse(saved, fund.getFundId(), fund.getNombre())));
    }

    private Mono<SubscriptionResponse> processCancellation(Client client, Subscription subscription, Fund fund) {
        return updateClientBalance(client.getClientId(), client.getSaldo().add(subscription.getMontoVinculado()))
                .then(Mono.defer(() -> subscriptionRepository.delete(subscription)))
                .then(saveTransaction(client.getClientId(), fund.getFundId(), subscription.getId(),
                        TransactionType.CANCELACION, subscription.getMontoVinculado()))
                .thenReturn(SubscriptionResponse.builder()
                        .id(subscription.getId())
                        .clientId(subscription.getClientId())
                        .fundId(fund.getFundId())
                        .fundName(fund.getNombre())
                        .montoVinculado(subscription.getMontoVinculado())
                        .fechaApertura(subscription.getFechaApertura())
                        .build());
    }

    private Mono<Client> updateClientBalance(String clientId, BigDecimal newBalance) {
        return clientRepository.findByClientId(clientId)
                .flatMap(c -> {
                    c.setSaldo(newBalance);
                    return clientRepository.save(c);
                });
    }

    private Mono<Transaction> saveTransaction(String clientId, String fundId, String subscriptionId,
                                              TransactionType tipo, BigDecimal monto) {
        Transaction tx = Transaction.builder()
                .transactionId(UUID.randomUUID().toString())
                .clientId(clientId)
                .fundId(fundId)
                .subscriptionId(subscriptionId)
                .tipo(tipo)
                .monto(monto)
                .fecha(Instant.now())
                .build();
        return transactionRepository.save(tx);
    }

    private SubscriptionResponse toResponse(Subscription sub, String fundId, String fundName) {
        return SubscriptionResponse.builder()
                .id(sub.getId())
                .clientId(sub.getClientId())
                .fundId(fundId)
                .fundName(fundName)
                .montoVinculado(sub.getMontoVinculado())
                .fechaApertura(sub.getFechaApertura())
                .build();
    }
}
