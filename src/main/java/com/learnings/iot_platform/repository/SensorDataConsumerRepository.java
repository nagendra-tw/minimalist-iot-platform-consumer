package com.learnings.iot_platform.repository;

import com.learnings.iot_platform.model.SensorData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SensorDataConsumerRepository extends MongoRepository<SensorData, String> {

    List<SensorData> findBySensorIdAndTimestampBetween(
            String sensorId, LocalDateTime timestamp, LocalDateTime timestamp2
    );

}
