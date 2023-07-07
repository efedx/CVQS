package com.gateway.config;

import com.gateway.filter.CustomAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.ObjectInputFilter;

@Configuration
public class GatewayConfig {

    @Bean
    public WebClient webClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder.build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder, CustomAuthenticationFilter customAuthenticationFilter) {

        return routeLocatorBuilder.routes()
                .route(r -> r
                        .path("/userManagement/updateEmployeeById/**")
                        .filters(f -> f.filter(customAuthenticationFilter.apply(new CustomAuthenticationFilter.Config())))
                        .uri("lb://EMPLOYEE")
                )
                .route(r -> r
                        .path("/userManagement/registerEmployee")
                        .uri("lb://EMPLOYEE")
                )
                .build();
    }

}
