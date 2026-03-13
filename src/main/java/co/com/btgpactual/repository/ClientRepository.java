package co.com.btgpactual.repository;

import co.com.btgpactual.model.Client;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ClientRepository extends ReactiveMongoRepository<Client, String> {

    Mono<Client> findByClientId(String clientId);

    Mono<Boolean> existsByClientId(String clientId);
}
