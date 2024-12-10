package com.learnings.iot_platform.unit.handler;

import com.learnings.iot_platform.events.SensorDataStoredEvent;
import com.learnings.iot_platform.handler.SensorDataStoredEventHandler;
import com.learnings.iot_platform.model.SensorData;
import com.learnings.iot_platform.service.SensorDataProcessService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Instant;
import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SensorDataStoredEventHandlerTest {

    @Mock
    private SensorDataProcessService sensorDataProcessService;

    private SensorDataStoredEventHandler sensorDataStoredEventHandler;

    @BeforeEach
    public void setUp() {
        this.sensorDataStoredEventHandler = new SensorDataStoredEventHandler(sensorDataProcessService);
    }

    @Test
    void testHandle_ShouldCallAnalyzeTemperatureAndAnalyzeBattery() {

        SensorDataStoredEvent sensorDataStoredEvent = new SensorDataStoredEvent("sensor-data-id-1", "sensor-1", 45.0d, 50d, 10.0d, 20.0d, LocalDateTime.now());
        SensorData sensorData = sensorDataStoredEventHandler.mapSensorDataStoredEventToSensorData(sensorDataStoredEvent);

        sensorDataStoredEventHandler.handle(sensorDataStoredEvent);

        verify(sensorDataProcessService).analyzeTemperature(sensorData);
        verify(sensorDataProcessService).analyzeBattery(sensorData);
    }

}
