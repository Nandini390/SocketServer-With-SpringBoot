package org.example.ubersocketserver.controllers;

import org.example.ubersocketserver.dtos.TestRequest;
import org.example.ubersocketserver.dtos.TestResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class TestController {

    @MessageMapping("/ping")
    @SendTo("/topic/ping")
    public TestResponse pingCheck(TestRequest message){
        return TestResponse.builder().data("Received").build();
    }
}
