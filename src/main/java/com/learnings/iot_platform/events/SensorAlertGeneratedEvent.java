package com.learnings.iot_platform.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorAlertGeneratedEvent {
    private String sensorId;
    private String alertType;
    private String message;
    private Instant timestamp;
}
