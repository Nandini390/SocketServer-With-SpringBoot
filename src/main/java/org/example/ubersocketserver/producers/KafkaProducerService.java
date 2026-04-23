package org.example.ubersocketserver.producers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.ubersocketserver.events.BookingLifecycleEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    public static final String DRIVER_LIFECYCLE_TOPIC = "driver.lifecycle";

    private final KafkaTemplate<String,String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void publishDriverLifecycleEvent(BookingLifecycleEvent event){
        try {
            kafkaTemplate.send(DRIVER_LIFECYCLE_TOPIC, event.getBookingId().toString(), objectMapper.writeValueAsString(event));
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Unable to publish driver lifecycle event", exception);
        }
    }
}
