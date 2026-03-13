package co.com.btgpactual.repository;

import co.com.btgpactual.model.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

/**
 * Reactive repository for Transaction documents.
 */
public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String> {

    Flux<Transaction> findByClientIdOrderByFechaDesc(String clientId);
}
