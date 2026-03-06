package org.example.ubersocketserver.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class chatRequest {
    private String name;
    private String message;
}
