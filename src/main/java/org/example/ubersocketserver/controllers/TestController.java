package org.example.ubersocketserver.controllers;

import org.example.ubersocketserver.dtos.TestRequest;
import org.example.ubersocketserver.dtos.TestResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
public class TestController {

    private SimpMessagingTemplate simpMessagingTemplate;

    public TestController(SimpMessagingTemplate simpMessagingTemplate){
        this.simpMessagingTemplate=simpMessagingTemplate;
    }

    @MessageMapping("/ping")
    @SendTo("/topic/ping")
    public TestResponse pingCheck(TestRequest message){
        System.out.println("received message from client: "+message.getData());
        return TestResponse.builder().data("Received").build();
    }

    @Scheduled(fixedDelay = 2000)
    public void sendPeriodicMessage(){
       simpMessagingTemplate.convertAndSend(
               "/topic/scheduled",
               "Periodic message sent"+System.currentTimeMillis());
    }
}
