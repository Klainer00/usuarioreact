package com.ecommerce.pedidos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${productos-service.url}")
    private String productosServiceUrl;

    @Bean
    public WebClient webClientProductos() {
        return WebClient.builder()
                .baseUrl(productosServiceUrl)
                .build();
    }
}
