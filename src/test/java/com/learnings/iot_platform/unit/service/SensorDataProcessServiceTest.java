package com.learnings.iot_platform.unit.service;

import com.learnings.iot_platform.constants.AlertTypes;
import com.learnings.iot_platform.model.SensorData;
import com.learnings.iot_platform.repository.SensorDataConsumerRepository;
import com.learnings.iot_platform.service.AlertService;
import com.learnings.iot_platform.service.SensorDataProcessService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SensorDataProcessServiceTest {

    @Mock
    private SensorDataConsumerRepository sensorDataConsumerRepository;

    @Mock
    private AlertService alertService;

    private SensorDataProcessService sensorDataProcessService;

    @BeforeEach
    public void setUp() {
        this.sensorDataProcessService = new SensorDataProcessService(
                this.sensorDataConsumerRepository,
                this.alertService);
    }

    @Test
    public void testAnalyzeTemperature_HighTemperatureAlert() {
        String sensorId = "sensor-1";
        SensorData currentReading = createMockSensorData(sensorId, 45.0, 50, 10.0, 20.0);

        List<SensorData> historicalReadings = Arrays.asList(
                createMockSensorData(sensorId, 41.0, 50, 10.0, 20.0),
                createMockSensorData(sensorId, 42.0, 50, 10.0, 20.0),
                createMockSensorData(sensorId, 43.0, 50, 10.0, 20.0)
        );

        when(sensorDataConsumerRepository.findBySensorIdAndTimestampBetween(
                eq(sensorId),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(historicalReadings);

        sensorDataProcessService.analyzeTemperature(currentReading);

        verify(alertService, times(1)).sendAlert(argThat(alert ->
                alert.getSensorId().equals(sensorId) &&
                        alert.getAlertType().equals(AlertTypes.HIGH_TEMPERATURE.toString())
        ));
    }

    private SensorData createMockSensorData(String sensorId, double temperature, double battery,
                                            double latitude, double longitude) {
        SensorData data = new SensorData();
        data.setSensorId(sensorId);
        data.setTemperature(temperature);
        data.setBattery(battery);
        data.setLatitude(latitude);
        data.setLongitude(longitude);
        data.setTimestamp(LocalDateTime.now());
        return data;
    }
}
