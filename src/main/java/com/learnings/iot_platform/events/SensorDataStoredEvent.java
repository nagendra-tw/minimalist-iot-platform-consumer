package com.learnings.iot_platform.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SensorDataStoredEvent {
    private String sensorId;
    private double temperature;
    private double latitude;
    private double longitude;
    private double battery;
    private LocalDateTime timestamp;


}
