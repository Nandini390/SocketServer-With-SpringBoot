package org.example.ubersocketserver.controllers;

import org.example.ubersocketserver.dtos.TestRequest;
import org.example.ubersocketserver.dtos.TestResponse;
import org.example.ubersocketserver.dtos.chatRequest;
import org.example.ubersocketserver.dtos.chatResponse;
import org.springframework.messaging.handler.annotation.DestinationVariable;
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
        return TestResponse.builder().data("Received: "+message.getData()).build();
    }

//    @Scheduled(fixedDelay = 2000)
//    public void sendPeriodicMessage(){
//       simpMessagingTemplate.convertAndSend(
//               "/topic/scheduled",
//               "Periodic message sent"+System.currentTimeMillis());
//    }

    @MessageMapping("/chat/{room}")
    @SendTo("/topic/message/{room}")
    public chatResponse chatMessage(@DestinationVariable String room, chatRequest request){
        return chatResponse.builder()
                .name(request.getName())
                .message(request.getMessage())
                .timeStamp(""+System.currentTimeMillis())
                .build();
    }

    @MessageMapping("/privateChat/{room}/{userId}")
//    @SendTo("/topic/privateMessage/{room}/{userId}")
    public void privateChatMessage(@DestinationVariable String room,@DestinationVariable String userId, chatRequest request){
        chatResponse response = chatResponse.builder()
                .name(request.getName())
                .message(request.getMessage())
                .timeStamp(""+System.currentTimeMillis())
                .build();
        simpMessagingTemplate.convertAndSendToUser(userId,"/queue/privateMessage/"+room, response);
    }
}

