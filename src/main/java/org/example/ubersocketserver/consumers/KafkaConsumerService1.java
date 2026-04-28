package org.example.ubersocketserver.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.ubersocketserver.events.DriverTrackingEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService1 {
    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @KafkaListener(topics = "driver.tracking",groupId = "driver-tracking-live-group")
    public void listen(String message){
        try {
            DriverTrackingEvent event = objectMapper.readValue(message, DriverTrackingEvent.class);
            simpMessagingTemplate.convertAndSend("/topic/bookings/" + event.getBookingId() + "/tracking", event);
        } catch (Exception exception) {
            System.out.println("kafka consumer new msg from topic driver.tracking: " + message);
        }
    }
}
