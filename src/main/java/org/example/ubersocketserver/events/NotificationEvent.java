package org.example.ubersocketserver.events;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class NotificationEvent {
    private UUID notificationId;
    private UUID recipientId;
    private UUID bookingId;
    private String eventType;
    private String title;
    private String message;
    private String source;
    private LocalDateTime occurredAt;
}
