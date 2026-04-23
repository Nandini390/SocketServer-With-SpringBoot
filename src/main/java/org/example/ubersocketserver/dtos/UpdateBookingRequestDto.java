package org.example.ubersocketserver.dtos;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBookingRequestDto {
    private String bookingStatus;
    private UUID driverId;
    private String cancellationReason;
}
