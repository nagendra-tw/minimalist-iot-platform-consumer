package com.learnings.iot_platform.service;

import com.learnings.iot_platform.events.SensorAlertGeneratedEvent;
import com.learnings.iot_platform.model.SensorAlert;
import com.learnings.iot_platform.producer.SensorAlertProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AlertService {
    private final SensorAlertProducerService sensorAlertProdcuerService;

    Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public AlertService(SensorAlertProducerService sensorAlertProducerService) {
        this.sensorAlertProdcuerService = sensorAlertProducerService;
    }

    public void sendAlert(SensorAlert alert){
        LOGGER.info("ALERT Generated: {} for Sensor {}", alert.getAlertType(), alert.getSensorId());
        SensorAlertGeneratedEvent sensorAlertGeneratedEvent = mapSensorAlertToSensorAlertGeneratedEvent(alert);
        sensorAlertProdcuerService.produceSensorAlertEvent(sensorAlertGeneratedEvent);
    }

    public SensorAlertGeneratedEvent mapSensorAlertToSensorAlertGeneratedEvent(SensorAlert alert){
        SensorAlertGeneratedEvent sensorAlertGeneratedEvent = new SensorAlertGeneratedEvent();
        sensorAlertGeneratedEvent.setAlertType(alert.getAlertType());
        sensorAlertGeneratedEvent.setSensorId(alert.getSensorId());
        sensorAlertGeneratedEvent.setTimestamp(alert.getTimestamp());
        sensorAlertGeneratedEvent.setMessage(alert.getMessage());
        return sensorAlertGeneratedEvent;
    }
}
