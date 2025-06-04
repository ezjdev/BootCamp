package com.colvir.bootcamp.homework16.producer.service;

import com.colvir.bootcamp.homework16.producer.client.BelarusBankClient;
import com.colvir.bootcamp.homework16.producer.dto.ExchangeRateDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.Message;
import lombok.SneakyThrows;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.activemq.ActiveMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.List;

import static com.colvir.bootcamp.homework16.producer.config.JmsConfig.QUEUE_NAME;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@Testcontainers
@SpringBootTest
@ContextConfiguration(classes = {
        ExchangeRateProducerService.class,
        ExchangeRateProducerServiceTest.BrokerConfig.class
})
class ExchangeRateProducerServiceTest {

    public static final String SELECTOR = "officeId = '2529'";
    public static final Duration AWAIT_TIMEOUT = Duration.ofSeconds(20);
    private static final String TEST_CITY = "Глубокое";
    private static final String ACTIVEMQ_DOCKER_IMAGE = "apache/activemq-classic";
    private static final int ACTIVE_MQ_PORT = 61616;
    @Container
    private static final ActiveMQContainer ACTIVE_MQ_CONTAINER =
            new ActiveMQContainer(ACTIVEMQ_DOCKER_IMAGE)
                    .withExposedPorts(ACTIVE_MQ_PORT)
                    .withEnv("ACTIVEMQ_ENABLE_SCHEDULER", "true");

    @MockitoBean
    private BelarusBankClient bankClient;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ExchangeRateProducerService underTest;

    @SneakyThrows
    @DisplayName("Producer can send message for listening another bean")
    @Test
    void sendMessageTest() {
        try (InputStream inputStream =
                     Thread.currentThread()
                             .getContextClassLoader()
                             .getResourceAsStream("exchangeRateDtoList.json")) {
            doReturn(new ObjectMapper().readValue(inputStream, new TypeReference<List<ExchangeRateDto>>() {
            })).when(bankClient).getExchangeRate(TEST_CITY);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        underTest.processExchangeRate();
        await().atMost(AWAIT_TIMEOUT).until(
                () -> {
                    Message message = jmsTemplate.receiveSelected(QUEUE_NAME, SELECTOR);
                    assertNotNull(message);
                    return true;
                }
        );
    }

    @EnableJms
    @Configuration
    static class BrokerConfig {

        private static final String BROKER_URL_TEMPLATE = "tcp://%s:%d";

        @Bean
        public JmsListenerContainerFactory<?> jmsListenerContainerFactory() {
            DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
            factory.setConnectionFactory(connectionFactory());
            return factory;
        }

        @Bean
        public ConnectionFactory connectionFactory() {
            return new ActiveMQConnectionFactory(BROKER_URL_TEMPLATE.formatted(
                    ACTIVE_MQ_CONTAINER.getHost(),
                    ACTIVE_MQ_CONTAINER.getFirstMappedPort()
            ));
        }

        @Bean
        public JmsTemplate jmsTemplate() {
            return new JmsTemplate(connectionFactory());
        }

        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }

        @Bean
        public JmsListenerContainerFactory<?> topicFactory(
                ConnectionFactory connectionFactory) {
            DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
            factory.setConnectionFactory(connectionFactory);
            factory.setPubSubDomain(true);
            return factory;
        }
    }

}