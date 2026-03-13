package co.com.btgpactual.repository;

import co.com.btgpactual.model.Subscription;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Reactive repository for Subscription documents.
 */
public interface SubscriptionRepository extends ReactiveMongoRepository<Subscription, String> {

    Mono<Subscription> findByClientIdAndFundId(String clientId, String fundId);

    Flux<Subscription> findByClientId(String clientId);

    Mono<Boolean> existsByClientIdAndFundId(String clientId, String fundId);

    Mono<Subscription> findByIdAndClientId(String id, String clientId);
}
