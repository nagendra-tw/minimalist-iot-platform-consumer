package com.learnings.iot_platform.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorAlert {
    private String sensorId;
    private String alertType;
    private String message;
    private Instant timestamp;
}
