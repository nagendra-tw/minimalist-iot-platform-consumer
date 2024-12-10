package com.learnings.iot_platform.unit.producer;

import com.learnings.iot_platform.constants.AlertTypes;
import com.learnings.iot_platform.constants.Constants;
import com.learnings.iot_platform.events.SensorAlertGeneratedEvent;
import com.learnings.iot_platform.producer.SensorAlertProducerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Instant;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SensorAlertProducerServiceTest {

    @Mock
    KafkaTemplate<String, Object> kafkaTemplate;

    SensorAlertProducerService sensorAlertProducerService;

    @BeforeEach
    void setUp() {
        sensorAlertProducerService = new SensorAlertProducerService(kafkaTemplate);
    }

    @Test
    void testProduceSensorAlert() {
        String sensorId = "sensor-id";
        SensorAlertGeneratedEvent sensorAlertGeneratedEvent = new SensorAlertGeneratedEvent();
        sensorAlertGeneratedEvent.setSensorId(sensorId);
        sensorAlertGeneratedEvent.setAlertType(AlertTypes.HIGH_TEMPERATURE.toString());
        sensorAlertGeneratedEvent.setMessage("ALERT: " + AlertTypes.HIGH_TEMPERATURE.toString() +  " for Sensor " + sensorId );
        sensorAlertGeneratedEvent.setTimestamp(LocalDateTime.now());

        sensorAlertProducerService.produceSensorAlertEvent(sensorAlertGeneratedEvent);

        verify(kafkaTemplate, times(1))
                .send(
                        eq(Constants.SENSOR_ALERT_GENERATED_EVENT_TOPIC),
                        eq(sensorId),
                        eq(sensorAlertGeneratedEvent)
                );
    }
}
