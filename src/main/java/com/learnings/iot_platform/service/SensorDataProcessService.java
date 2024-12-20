package com.learnings.iot_platform.service;

import com.learnings.iot_platform.constants.AlertTypes;
import com.learnings.iot_platform.constants.Constants;
import com.learnings.iot_platform.model.SensorAlert;
import com.learnings.iot_platform.model.SensorData;
import com.learnings.iot_platform.repository.SensorDataConsumerRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SensorDataProcessService {


    private final SensorDataConsumerRepository sensorDataConsumerRepository;
    private final AlertService alertService;

    public SensorDataProcessService(SensorDataConsumerRepository sensorDataConsumerRepository, AlertService alertService) {
        this.sensorDataConsumerRepository = sensorDataConsumerRepository;
        this.alertService = alertService;
    }

    public void analyzeTemperature(SensorData currentReading) {
        LocalDateTime currentReadingTimestamp = currentReading.getTimestamp();
        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);

        List<SensorData> recentReadings = sensorDataConsumerRepository.findBySensorIdAndTimestampBetween(
                currentReading.getSensorId(),
                fiveMinutesAgo,
                currentReadingTimestamp
        );

        double avgTemperature = recentReadings
                .stream()
                .mapToDouble(SensorData::getTemperature)
                .average()
                .orElse(0.0);

        if(avgTemperature > Constants.AVERAGE_TEMPERATURE_THRESHOLD) {
            generateTemperatureAlert(
                    currentReading.getSensorId(),
                    avgTemperature
            );
        }
    }

    public void analyzeBattery(SensorData currentReading) {
        if(currentReading.getBattery() <= Constants.LOW_BATTERY_THRESHOLD) {
            generateBatteryAlert(currentReading.getSensorId(), currentReading.getBattery());
        }
    }

    private void generateBatteryAlert(String sensorId, Double battery) {
        SensorAlert sensorAlert = SensorAlert.builder()
                .sensorId(sensorId)
                .alertType(AlertTypes.LOW_BATTERY.toString())
                .message("Battery level is critically low at " + battery + "%.")
                .timestamp(LocalDateTime.now())
                .build();

        alertService.sendAlert(sensorAlert);
    }

    private void generateTemperatureAlert(String sensorId, double avgTemperature) {
        SensorAlert sensorAlert = new SensorAlert();
        sensorAlert.setSensorId(sensorId);
        sensorAlert.setAlertType(AlertTypes.HIGH_TEMPERATURE.toString());
        sensorAlert.setMessage(String.format("5-min avg temperature %.2f°C exceeds 40°C", avgTemperature));
        sensorAlert.setTimestamp(LocalDateTime.now());

        alertService.sendAlert(sensorAlert);
    }


}
