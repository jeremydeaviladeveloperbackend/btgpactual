package co.com.btgpactual.repository;

import co.com.btgpactual.model.Fund;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

/**
 * Reactive repository for Fund documents.
 */
public interface FundRepository extends ReactiveMongoRepository<Fund, String> {

    Mono<Fund> findByFundId(String fundId);
}
