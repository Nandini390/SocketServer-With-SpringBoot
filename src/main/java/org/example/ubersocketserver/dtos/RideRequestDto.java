package org.example.ubersocketserver.dtos;

import lombok.*;
import org.example.ubersocketserver.Models.ExactLocation;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RideRequestDto {
    private Long passengerId;
    private ExactLocation startLocation;
    private ExactLocation endLocation;
    private List<Long> driverIds;
    private Long bookingId;
}
