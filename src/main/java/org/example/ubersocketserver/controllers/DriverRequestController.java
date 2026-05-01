package org.example.ubersocketserver.controllers;

import org.example.ubersocketserver.dtos.RideRequestDto;
import org.example.ubersocketserver.dtos.RideResponseDto;
import org.example.ubersocketserver.dtos.DriverDecisionRequestDto;
import org.example.ubersocketserver.dtos.UpdateBookingRequestDto;
import org.example.ubersocketserver.dtos.UpdateBookingResponseDto;
import org.example.ubersocketserver.events.BookingLifecycleEvent;
import org.example.ubersocketserver.producers.KafkaProducerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/socket")
public class DriverRequestController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final RestTemplate restTemplate;
    private final KafkaProducerService kafkaProducerService;

    public DriverRequestController(SimpMessagingTemplate simpMessagingTemplate, KafkaProducerService kafkaProducerService){
        this.simpMessagingTemplate=simpMessagingTemplate;
        this.restTemplate=new RestTemplate();
        this.kafkaProducerService=kafkaProducerService;
    }

    @PostMapping("/newRide")
    public ResponseEntity<Boolean> raiseRideRequest(@RequestBody RideRequestDto requestDto){
       sendDriversNewRideRequest(requestDto);
       kafkaProducerService.publishDriverLifecycleEvent(BookingLifecycleEvent.builder()
               .bookingId(requestDto.getBookingId())
               .passengerId(requestDto.getPassengerId())
               .driverId(null)
               .eventType("RIDE_REQUEST_DISPATCHED")
               .bookingStatus("ASSIGNING_DRIVER")
               .source("UberSocketServer")
               .message("Ride request dispatched to candidate drivers")
               .occurredAt(LocalDateTime.now())
               .build());
       return new ResponseEntity<>(Boolean.TRUE,HttpStatus.OK);
    }

    public void sendDriversNewRideRequest(RideRequestDto requestDto){
        if (requestDto.getDriverIds() == null || requestDto.getDriverIds().isEmpty()) {
            return;
        }
        requestDto.getDriverIds().forEach(driverId ->
                simpMessagingTemplate.convertAndSend("/topic/rideRequest/" + driverId, requestDto)
        );
    }

    @MessageMapping("/rideResponse/{userId}")
    public synchronized void rideResponseHandler(@DestinationVariable String userId , RideResponseDto responseDto){
        if (!Boolean.TRUE.equals(responseDto.response)) {
            DriverDecisionRequestDto rejectRequest = DriverDecisionRequestDto.builder()
                    .driverId(UUID.fromString(userId))
                    .reason(responseDto.reason)
                    .build();
            this.restTemplate.postForEntity(
                    "http://localhost:8089/api/v1/booking/" + responseDto.bookingId + "/reject-driver",
                    rejectRequest,
                    UpdateBookingResponseDto.class
            );
            return;
        }

        UpdateBookingRequestDto requestDto=UpdateBookingRequestDto.builder()
                                           .driverId(UUID.fromString(userId))
                                           .bookingStatus("ACCEPTED")
                                           .build();

        ResponseEntity<UpdateBookingResponseDto> result = this.restTemplate.postForEntity(
                "http://localhost:8089/api/v1/booking/" + responseDto.bookingId + "/assign-driver",
                requestDto,
                UpdateBookingResponseDto.class
        );
        kafkaProducerService.publishDriverLifecycleEvent(BookingLifecycleEvent.builder()
                .bookingId(responseDto.bookingId)
                .passengerId(result.getBody() != null ? result.getBody().getPassengerId() : null)
                .driverId(UUID.fromString(userId))
                .eventType("DRIVER_ACCEPTED")
                .bookingStatus(result.getBody() != null && result.getBody().getBookingStatus() != null ? result.getBody().getBookingStatus().name() : "ACCEPTED")
                .source("UberSocketServer")
                .message("Driver accepted booking through socket response")
                .occurredAt(LocalDateTime.now())
                .build());
        System.out.println(result.getStatusCode());
    }
}
