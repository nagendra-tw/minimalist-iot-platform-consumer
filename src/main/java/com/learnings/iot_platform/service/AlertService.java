package com.learnings.iot_platform.service;

import com.learnings.iot_platform.model.SensorAlert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AlertService {
    Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public void sendAlert(SensorAlert alert){
        // todo: send this alert to a new sensor alert topic

        LOGGER.info("ALERT Generated: {} for Sensor {}", alert.getAlertType(), alert.getSensorId());
    }
}
