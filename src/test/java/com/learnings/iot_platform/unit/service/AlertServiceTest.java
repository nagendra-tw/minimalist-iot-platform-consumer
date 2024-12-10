package com.learnings.iot_platform.unit.service;

import com.learnings.iot_platform.constants.AlertTypes;
import com.learnings.iot_platform.events.SensorAlertGeneratedEvent;
import com.learnings.iot_platform.model.SensorAlert;
import com.learnings.iot_platform.producer.SensorAlertProducerService;
import com.learnings.iot_platform.service.AlertService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AlertServiceTest {

    @Mock
    SensorAlertProducerService sensorAlertProducerService;

    private AlertService alertService;

    @BeforeEach
    void setUp() {
        this.alertService = new AlertService(sensorAlertProducerService);
    }

    @Test
    void testSendAlert() {
        String sensorId = "sensorId";
        SensorAlert sensorAlert = new SensorAlert();
        sensorAlert.setSensorId(sensorId);
        sensorAlert.setTimestamp(Instant.now());
        sensorAlert.setAlertType(AlertTypes.HIGH_TEMPERATURE.toString());
        sensorAlert.setMessage("ALERT: " + AlertTypes.HIGH_TEMPERATURE +  " for Sensor " + sensorId );

        SensorAlertGeneratedEvent sensorAlertGeneratedEvent = new SensorAlertGeneratedEvent();
        sensorAlertGeneratedEvent.setMessage(sensorAlert.getMessage());
        sensorAlertGeneratedEvent.setTimestamp(sensorAlert.getTimestamp());
        sensorAlertGeneratedEvent.setAlertType(sensorAlert.getAlertType());
        sensorAlertGeneratedEvent.setSensorId(sensorId);

        alertService.sendAlert(sensorAlert);

        verify(sensorAlertProducerService, times(1))
                .produceSensorAlertEvent(sensorAlertGeneratedEvent);
    }
}
