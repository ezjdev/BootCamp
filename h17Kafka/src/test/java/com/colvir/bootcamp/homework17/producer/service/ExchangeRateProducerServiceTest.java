package com.colvir.bootcamp.homework17.producer.service;

import com.colvir.bootcamp.homework17.dto.ExchangeRateDto;
import com.colvir.bootcamp.homework17.producer.client.BelarusBankClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static com.colvir.bootcamp.homework17.config.KafkaConfig.GROUP_ID;
import static com.colvir.bootcamp.homework17.config.KafkaConfig.TOPIC_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@Slf4j
@Testcontainers
@SpringBootTest
@ContextConfiguration(classes = {
        ExchangeRateProducerService.class,
        ExchangeRateProducerServiceTest.BrokerConfig.class
})
class ExchangeRateProducerServiceTest {

    public static final String TEST_CITY = "Глубокое";
    private static final String KAFKA_DOCKER_IMAGE = "apache/kafka-native";

    @Container
    private static final KafkaContainer KAFKA_CONTAINER =
            new KafkaContainer(KAFKA_DOCKER_IMAGE);
    public static final String RESET_CONFIG = "earliest";
    private static final Duration AWAIT_TIMEOUT = Duration.ofSeconds(10);

    @MockitoBean
    private BelarusBankClient bankClient;

    @Autowired
    private ExchangeRateProducerService underTest;

    private static final BlockingQueue<String> messages = new LinkedBlockingQueue<>();

    @KafkaListener(topics = TOPIC_NAME)
    public void listen(String message) {
        messages.add(message);
    }

    @Test
    @DisplayName("Producer can send message for listening another bean")
    void sendMessageTest() {
        try (InputStream inputStream =
                     Thread.currentThread()
                             .getContextClassLoader()
                             .getResourceAsStream("exchangeRateDtoList.json")) {
            doReturn(new ObjectMapper().readValue(inputStream, new TypeReference<List<ExchangeRateDto>>() {
            }))
                    .when(bankClient).getExchangeRate(TEST_CITY);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        underTest.processExchangeRate();

        await().atMost(AWAIT_TIMEOUT).until(() -> {
                    ExchangeRateDto rateDto =
                            new ObjectMapper().readValue(messages.poll()
                                    , ExchangeRateDto.class);
                    log.info("received: {}", rateDto);
                    assertThat(rateDto).isNotNull()
                            .extracting(ExchangeRateDto::getName).isEqualTo(TEST_CITY);
                    return true;
                }
        );

    }

    @EnableKafka
    @Configuration
    static class BrokerConfig {

        @Bean
        public ProducerFactory<String, String> getProducerConfig() {
            return new DefaultKafkaProducerFactory<>(
                    Map.of(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_CONTAINER.getBootstrapServers(),
                            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class)
            );
        }

        @Bean
        public KafkaTemplate<String, String> kafkaTemplate() {
            return new KafkaTemplate<>(getProducerConfig());
        }

        @Bean
        public ConsumerFactory<String, String> consumerFactory() {
            return new DefaultKafkaConsumerFactory<>(
                    Map.of(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_CONTAINER.getBootstrapServers(),
                            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                            ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID,
                            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, RESET_CONFIG
                    )
            );
        }

        @Bean
        public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
            ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
            factory.setConsumerFactory(consumerFactory());
            return factory;
        }

        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }

    }

}