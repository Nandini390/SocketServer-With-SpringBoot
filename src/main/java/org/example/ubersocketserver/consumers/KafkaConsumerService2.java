package org.example.ubersocketserver.consumers;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService2 {
    @KafkaListener(topics = "payment.lifecycle", groupId = "payment-lifecycle-audit-group")
    public void listen(String message){
        System.out.println("kafka msg from topic payment.lifecycle: " + message);
    }
}
