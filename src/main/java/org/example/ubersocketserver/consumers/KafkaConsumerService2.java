package org.example.ubersocketserver.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.ubersocketserver.events.NotificationEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService2 {
    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @KafkaListener(topics = "notification.lifecycle", groupId = "notification-lifecycle-live-group")
    public void listen(String message){
        try {
            NotificationEvent event = objectMapper.readValue(message, NotificationEvent.class);
            simpMessagingTemplate.convertAndSend("/topic/users/" + event.getRecipientId() + "/notifications", event);
        } catch (Exception exception) {
            System.out.println("kafka msg from topic notification.lifecycle: " + message);
        }
    }
}
