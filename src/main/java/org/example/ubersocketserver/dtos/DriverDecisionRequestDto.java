package org.example.ubersocketserver.dtos;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverDecisionRequestDto {
    private UUID driverId;
    private String reason;
}
