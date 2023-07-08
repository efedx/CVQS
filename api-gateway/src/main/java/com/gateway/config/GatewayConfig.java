package com.gateway.config;

import com.gateway.filter.CustomAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.loadbalancer.blocking.client.BlockingLoadBalancerClient;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
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
//    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder, CustomAuthenticationFilter customAuthenticationFilter) {

        return routeLocatorBuilder.routes()

                .route(r -> r
                        .path("/registerAdmin")
                        .uri("lb://EMPLOYEE")
                )
                .route(r -> r
                        .path("/login")
                        .uri("lb://EMPLOYEE")
                )
                .route(r -> r
                        .path("/userManagement/**")
                        .filters(f -> f.filter(customAuthenticationFilter.apply(new CustomAuthenticationFilter.Config())))
                        .uri("lb://EMPLOYEE")
                )
                .route(r -> r
                        .path("/terminals/**")
                        .filters(f -> f.filter(customAuthenticationFilter.apply(new CustomAuthenticationFilter.Config())))
                        .uri("lb://TERMINAL")
                )
                .route(r -> r
                        .path("/defects/**")
                        .filters(f -> f.filter(customAuthenticationFilter.apply(new CustomAuthenticationFilter.Config())))
                        .uri("lb://DEFECT")
                )
                .route(r -> r
                        .path("/registerDefects")
                        .filters(f -> f.filter(customAuthenticationFilter.apply(new CustomAuthenticationFilter.Config())))
                        .uri("lb://DEFECT")
                )
                .build();
    }

}
