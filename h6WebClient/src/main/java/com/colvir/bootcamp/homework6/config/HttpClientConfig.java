package com.colvir.bootcamp.homework6.config;

import com.colvir.bootcamp.homework6.api.PlaylistClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class HttpClientConfig {
    @Bean
    public PlaylistClient apiClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl("http://localhost:8090/api/playlist")
                .defaultHeader("Content-Type", "application/json")
                .build();

        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(PlaylistClient.class);
    }
}
