package org.example.ubersocketserver.consumers;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService1 {
    @KafkaListener(topics = "driver.lifecycle",groupId = "driver-lifecycle-audit-group")
    public void listen(String message){
        System.out.println("kafka consumer new msg from topic driver.lifecycle: "+message);
    }
}
