package co.com.btgpactual.service;

import co.com.btgpactual.model.Client;
import co.com.btgpactual.model.Fund;
import co.com.btgpactual.model.NotificationPreference;
import co.com.btgpactual.model.Subscription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Servicio de notificaciones simulado.
 * En producción se integraría con SendGrid, Twilio, AWS SNS, etc.
 */
@Slf4j
@Service
public class NotificationService {

    /**
     * Sends a subscription notification (email or SMS) based on client preference.
     * Currently simulated via logging.
     *
     * @param client       the client who subscribed
     * @param fund         the fund subscribed to
     * @param subscription the created subscription
     * @return Mono completing when notification is sent
     */
    public Mono<Void> notifySubscription(Client client, Fund fund, Subscription subscription) {
        return Mono.fromRunnable(() -> {
            if (client.getPreferenciaNotificacion() == NotificationPreference.EMAIL) {
                log.info("[SIMULACIÓN EMAIL] Enviando email a cliente {} - Suscripción exitosa al fondo {} por COP {}",
                        client.getClientId(), fund.getNombre(), subscription.getMontoVinculado());
            } else {
                log.info("[SIMULACIÓN SMS] Enviando SMS a cliente {} - Suscripción exitosa al fondo {} por COP {}",
                        client.getClientId(), fund.getNombre(), subscription.getMontoVinculado());
            }
        });
    }
}
