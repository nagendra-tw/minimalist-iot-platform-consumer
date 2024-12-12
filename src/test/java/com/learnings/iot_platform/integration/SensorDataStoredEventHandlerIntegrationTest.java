package com.learnings.iot_platform.integration;

import com.learnings.iot_platform.constants.Constants;
import com.learnings.iot_platform.events.SensorDataStoredEvent;
import com.learnings.iot_platform.handler.SensorDataStoredEventHandler;
import com.learnings.iot_platform.model.SensorData;
import com.learnings.iot_platform.service.SensorDataProcessService;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DirtiesContext
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EmbeddedKafka(partitions = 3, count = 3, controlledShutdown = false)
@SpringBootTest(properties = "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}")
@AutoConfigureMockMvc
@EnableKafka
public class SensorDataStoredEventHandlerIntegrationTest {

    public static final String TOPIC = Constants.SENSOR_DATA_STORED_EVENT_TOPIC;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    private SensorDataStoredEventHandler sensorDataStoredEventHandler;

    @Mock
    private SensorDataProcessService sensorDataProcessService;

    private KafkaTemplate<String, SensorDataStoredEvent> kafkaTemplate;

    private KafkaMessageListenerContainer<String, SensorDataStoredEvent> container;
    private BlockingQueue<ConsumerRecord<String, SensorDataStoredEvent>> records;


    @BeforeEach
    public void setup() {
        DefaultKafkaConsumerFactory<String, Object> consumerFactory = new DefaultKafkaConsumerFactory<>(getConsumerProperties());
        ContainerProperties containerProperties = new ContainerProperties(Constants.SENSOR_DATA_STORED_EVENT_TOPIC);
        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
        records = new LinkedBlockingQueue<>();
        container.setupMessageListener((MessageListener<String, SensorDataStoredEvent>) records::add);
        container.start();
        ContainerTestUtils.waitForAssignment(container, embeddedKafkaBroker.getPartitionsPerTopic());

        sensorDataStoredEventHandler = new SensorDataStoredEventHandler(sensorDataProcessService);

        this.kafkaTemplate = createKafkaTemplate();

    }

    @Test
    void testHandleSensorDataStoredEvent() throws InterruptedException {
        SensorDataStoredEvent event = new SensorDataStoredEvent();
        event.setTimestamp(LocalDateTime.now());
        event.setTemperature(22.5);
        event.setBattery(80);
        event.setLatitude(12.34);
        event.setLongitude(56.78);
        event.setSensorId("sensor-123");
        event.setSensorDataId("data-123");

        kafkaTemplate.send(TOPIC, "key", event);

        ConsumerRecord<String, SensorDataStoredEvent> record = records.poll(3000, TimeUnit.MILLISECONDS);
        assert record != null;
        SensorDataStoredEvent receivedEvent = (SensorDataStoredEvent) record.value();
        assertNotNull(receivedEvent);

        sensorDataStoredEventHandler.handle(receivedEvent);
        verify(sensorDataProcessService, times(1)).analyzeTemperature(any(SensorData.class));
        verify(sensorDataProcessService, times(1)).analyzeBattery(any(SensorData.class));
    }

    private Map<String, Object> getConsumerProperties() {
        return Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, embeddedKafkaBroker.getBrokersAsString(),
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
                ConsumerConfig.GROUP_ID_CONFIG, "test-group",
                JsonDeserializer.TRUSTED_PACKAGES,"*",
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"
        );
    }

    private KafkaTemplate<String, SensorDataStoredEvent> createKafkaTemplate() {
        Map<String, Object> producerProps = KafkaTestUtils.producerProps(embeddedKafkaBroker);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class); // Set key serializer to StringSerializer

        DefaultKafkaProducerFactory<String, SensorDataStoredEvent> producerFactory = new DefaultKafkaProducerFactory<>(producerProps);

        return new KafkaTemplate<>(producerFactory);
    }
}
