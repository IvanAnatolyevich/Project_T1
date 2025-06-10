package ru.yandex.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class MainServiceConfig {
    @Value("${analysis-service.base-url}")
    private String analysisServiceBaseUrl;

    @Value("${analysis-service.username}")
    private String username;

    @Value("${analysis-service.password}")
    private String password;

    @Bean
    public WebClient analysisServiceWebClient() {
        return WebClient.builder()
                .baseUrl(analysisServiceBaseUrl)
                .defaultHeaders(headers -> headers.setBasicAuth(username, password))
                .build();
    }
}
