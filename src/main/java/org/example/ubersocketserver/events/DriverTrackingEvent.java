package org.example.ubersocketserver.events;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DriverTrackingEvent {
    private String bookingId;
    private String driverId;
    private Double latitude;
    private Double longitude;
    private String trackingStage;
    private Long actualDistanceMeters;
    private LocalDateTime occurredAt;
}
