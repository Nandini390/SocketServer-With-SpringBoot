package org.example.ubersocketserver.dtos;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RideResponseDto {
    public Boolean response;
    public UUID bookingId;
    public String reason;
}
