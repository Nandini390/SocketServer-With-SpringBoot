package org.example.ubersocketserver.dtos;

import lombok.*;
import org.springframework.messaging.handler.annotation.SendTo;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class chatResponse {
    private String name;
    private String message;
    private String timeStamp;
}
