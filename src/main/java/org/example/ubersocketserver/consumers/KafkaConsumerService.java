package org.example.ubersocketserver.consumers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @KafkaListener(topics = "booking.lifecycle")
    public void listen(String message){
        try {
            JsonNode event = objectMapper.readTree(message);
            String bookingId = event.path("bookingId").asText();
            simpMessagingTemplate.convertAndSend("/topic/bookings/" + bookingId + "/status", event);
        } catch (Exception exception) {
            System.out.println("kafka msg from topic booking.lifecycle: " + message);
        }
    }
}
