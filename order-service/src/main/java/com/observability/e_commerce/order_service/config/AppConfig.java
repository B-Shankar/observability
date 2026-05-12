package com.observability.e_commerce.order_service.config;

import org.slf4j.MDC;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {

        return builder
                .additionalInterceptors((request, body, execution) -> {

                    String requestId = MDC.get("requestId");

                    if (requestId != null) {
                        request.getHeaders()
                                .add("X-Request-Id", requestId);
                    }

                    return execution.execute(request, body);
                })
                .build();
    }
}