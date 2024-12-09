package com.learnings.iot_platform.handler;

import com.learnings.iot_platform.constants.Constants;
import com.learnings.iot_platform.events.SensorDataStoredEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = Constants.SENSOR_DATA_STORED_EVENT_TOPIC)
public class SensorDataStoredEventHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @KafkaHandler
    public void handle(SensorDataStoredEvent sensorDataStoredEvent) {
        LOGGER.info("Received SensorDataStoredEvent {}", sensorDataStoredEvent);
    }
}
