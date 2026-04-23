package org.example.ubersocketserver.dtos;

import lombok.*;
import org.example.uberprojectentityservice.Models.ExactLocation;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RideRequestDto {
    private UUID passengerId;
    private ExactLocation startLocation;
    private ExactLocation endLocation;
    private List<UUID> driverIds;
    private UUID bookingId;
}
