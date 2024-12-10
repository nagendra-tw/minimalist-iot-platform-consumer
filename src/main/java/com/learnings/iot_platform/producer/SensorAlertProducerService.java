package com.learnings.iot_platform.producer;

import com.learnings.iot_platform.constants.Constants;
import com.learnings.iot_platform.events.SensorAlertGeneratedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class SensorAlertProducerService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public SensorAlertProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void produceSensorAlertEvent(SensorAlertGeneratedEvent sensorAlertGeneratedEvent) {
        LOGGER.info("Going to publish sensor alert generated event: {}", sensorAlertGeneratedEvent);

        kafkaTemplate.send(
                Constants.SENSOR_ALERT_GENERATED_EVENT_TOPIC,
                sensorAlertGeneratedEvent.getSensorId(),
                sensorAlertGeneratedEvent);

        LOGGER.info("Published sensor alert generated event: {}", sensorAlertGeneratedEvent);
    }
}
