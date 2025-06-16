package com.colvir.bootcamp.homework5.config;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
@EnableCaching(order = Ordered.HIGHEST_PRECEDENCE)
public class CacheConfiguration {

    @Bean
    HazelcastInstance getHazelcastClientInstance() {
        ClientConfig clientConfig = new ClientConfig();

        clientConfig.getSerializationConfig()
                .getCompactSerializationConfig()
                .addSerializer(new SqlTimeCompactSerializer());

        return HazelcastClient.newHazelcastClient(clientConfig);
    }

    @Bean
    CacheManager cacheManager(HazelcastInstance hazelcastInstance) {
        return new HazelcastCacheManager(hazelcastInstance);
    }

}
