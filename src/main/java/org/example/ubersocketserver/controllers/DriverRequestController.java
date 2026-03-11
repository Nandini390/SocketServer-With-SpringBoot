package org.example.ubersocketserver.controllers;

import org.example.ubersocketserver.dtos.RideRequestDto;
import org.example.ubersocketserver.dtos.RideResponseDto;
import org.example.ubersocketserver.dtos.UpdateBookingRequestDto;
import org.example.ubersocketserver.dtos.UpdateBookingResponseDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Controller
@RequestMapping("/api/socket")
public class DriverRequestController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final RestTemplate restTemplate;

    public DriverRequestController(SimpMessagingTemplate simpMessagingTemplate){
        this.simpMessagingTemplate=simpMessagingTemplate;
        this.restTemplate=new RestTemplate();
    }

    @PostMapping("/newRide")
    public ResponseEntity<Boolean> raiseRideRequest(@RequestBody RideRequestDto requestDto){
       sendDriversNewRideRequest(requestDto);
       return new ResponseEntity<>(Boolean.TRUE,HttpStatus.OK);
    }

    public void sendDriversNewRideRequest(RideRequestDto requestDto){
        //ToDo: ideally the request should only send to nearby drivers, but for simplicity we have sended to everyone
       simpMessagingTemplate.convertAndSend("/topic/rideRequest", requestDto);
    }

    @MessageMapping("/rideResponse/{userId}")
    public synchronized void rideResponseHandler(@DestinationVariable String userId , RideResponseDto responseDto){
        System.out.println(responseDto.getResponse()+" "+userId);
        UpdateBookingRequestDto requestDto=UpdateBookingRequestDto.builder()
                                           .driverId(Optional.of(Long.parseLong(userId)))
                                           .bookingStatus("SCHEDULED")
                                           .build();
        ResponseEntity<UpdateBookingResponseDto> result = this.restTemplate.postForEntity("http://localhost:8080/api/v1/booking/"+responseDto.bookingId,requestDto, UpdateBookingResponseDto.class);
        System.out.println(result.getStatusCode());
    }
}
