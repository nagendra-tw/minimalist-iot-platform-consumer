package com.learnings.iot_platform.handler;

import com.learnings.iot_platform.constants.Constants;
import com.learnings.iot_platform.events.SensorDataStoredEvent;
import com.learnings.iot_platform.model.SensorData;
import com.learnings.iot_platform.service.SensorDataProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = Constants.SENSOR_DATA_STORED_EVENT_TOPIC)
public class SensorDataStoredEventHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final SensorDataProcessService sensorDataProcessService;

    public SensorDataStoredEventHandler(SensorDataProcessService sensorDataProcessService) {
        this.sensorDataProcessService = sensorDataProcessService;
    }

    @KafkaHandler
    public void handle(SensorDataStoredEvent sensorDataStoredEvent) {
        LOGGER.info("Received SensorDataStoredEvent {}", sensorDataStoredEvent);

        SensorData sensorData = mapSensorDataStoredEventToSensorData(sensorDataStoredEvent);
        sensorDataProcessService.analyzeTemperature(sensorData);
        sensorDataProcessService.analyzeBattery(sensorData);

    }

    public SensorData mapSensorDataStoredEventToSensorData(SensorDataStoredEvent sensorDataStoredEvent) {
        SensorData sensorData = new SensorData();
        sensorData.setTimestamp(sensorDataStoredEvent.getTimestamp());
        sensorData.setTemperature(sensorDataStoredEvent.getTemperature());
        sensorData.setLatitude(sensorDataStoredEvent.getLatitude());
        sensorData.setLongitude(sensorDataStoredEvent.getLongitude());
        sensorData.setSensorId(sensorDataStoredEvent.getSensorId());
        sensorData.setSensorDataId(sensorDataStoredEvent.getSensorDataId());

        return sensorData;
    }
}
