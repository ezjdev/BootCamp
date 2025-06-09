package com.colvir.bootcamp.homework17.producer.service;

import com.colvir.bootcamp.homework17.consumer.service.ExchangeRateConsumerService;
import com.colvir.bootcamp.homework17.dto.ExchangeRateDto;
import com.colvir.bootcamp.homework17.producer.client.BelarusBankClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@Slf4j
@Testcontainers
@SpringBootTest
@DirtiesContext
@EmbeddedKafka
class ExchangeRateProducerServiceTest {

    public static final String TEST_CITY = "Глубокое";
    private static final String KAFKA_DOCKER_IMAGE = "apache/kafka-native";

    @Container
    private static final KafkaContainer KAFKA_CONTAINER =
            new KafkaContainer(KAFKA_DOCKER_IMAGE);
    private static final Duration AWAIT_TIMEOUT = Duration.ofSeconds(10);

    @MockitoBean
    private BelarusBankClient bankClient;

    @Autowired
    private ExchangeRateConsumerService consumerService;

    @Autowired
    private ExchangeRateProducerService underTest;

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

        await().atMost(AWAIT_TIMEOUT).until(() -> !consumerService.getReceivedExchangeRateList().isEmpty());

        assertThat(consumerService.getReceivedExchangeRateList().get(0)).isNotNull()
                .extracting(ExchangeRateDto::getName).isEqualTo(TEST_CITY);
    }

}