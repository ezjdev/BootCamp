package com.colvir.bootcamp.homework5.init;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Component
@Slf4j
public class SchemaInitializer implements BeanPostProcessor {

    @Value("${spring.liquibase.default-schema}")
    private String schemaName;

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (bean instanceof DataSource dataSource) {
            try (Connection conn = dataSource.getConnection();
                 Statement statement = conn.createStatement();
            ) {
                statement.execute(String.format("CREATE SCHEMA IF NOT EXISTS %s", schemaName));
            } catch (SQLException e) {
                log.error("Can't create schema", e);
            }
        }
        return bean;
    }
}