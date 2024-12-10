package com.learnings.iot_platform.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SensorAlert {
    private String sensorId;
    private String alertType;
    private String message;
    private LocalDateTime timestamp;
}
