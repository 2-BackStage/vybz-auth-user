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

        // 네트워크 안될 때 처리 메시지
        // 에러 헨들러는 kafkaTemplate 안에 있다
        try {
            CompletableFuture<SendResult<String, UserAuthEvent>> future = kafkaTemplate.send(CREATE_USER_TOPIC, event);

            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("[Kafka] Failed to send UserAuthEvent: {}", ex.getMessage(), ex);

                    // 여기서 에러 헨들러 처리하기 !

                } else {
                    log.info("[Kafka] Successfully sent UserAuthEvent. Topic: {}, Partition: {}, Offset: {}",
                            result.getRecordMetadata().topic(),
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset());
                }
            });
        } catch (Exception e) {
            log.error("[Kafka] Failed to send UserAuthEvent: {}", e.getMessage(), e);
        }
    }
}
