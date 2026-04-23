package org.example.ubersocketserver.dtos;

import lombok.*;
import org.example.uberprojectentityservice.Models.BookingStatus;
import org.example.uberprojectentityservice.Models.Driver;

import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBookingResponseDto {
    private UUID bookingId;
    private BookingStatus bookingStatus;
    private Optional<Driver> driver;
    private UUID passengerId;
}
