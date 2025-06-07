package back.vybz.auth_user.kafka.producer;

import back.vybz.auth_user.kafka.event.UserAuthEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserKafkaProducer {

    private final KafkaTemplate<String, UserAuthEvent> kafkaTemplate;

    public static final String CREATE_USER_TOPIC = "create-user-auth";

    public void sendUserAuthEvent(UserAuthEvent event) {
        log.info("[Kafka] Sending UserAuthEvent to topic '{}': {}", CREATE_USER_TOPIC, event);
        CompletableFuture<SendResult<String, UserAuthEvent>> future =
                kafkaTemplate.send(CREATE_USER_TOPIC, event);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("[Kafka] Failed to send UserAuthEvent: {}", ex.getMessage(), ex);
            } else {
                log.info("[Kafka] Successfully sent UserAuthEvent with offset: {}", result.getRecordMetadata().offset());
            }
        });
    }
}
