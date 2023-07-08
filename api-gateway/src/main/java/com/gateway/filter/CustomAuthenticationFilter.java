package com.gateway.filter;

import com.securityClient.SecurityClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class CustomAuthenticationFilter extends AbstractGatewayFilterFactory<CustomAuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;
    @Autowired
    private SecurityClient securityClient;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private WebClient webClient;

    public CustomAuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {

        return new OrderedGatewayFilter((exchange, chain) -> {

            if (true) { // validator.isSecured.test(exchange.getRequest())
                //header contains token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("missing authorization header");
                }


                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

                String contextPath = exchange.getRequest().getPath().value();
                String determiner = contextPath.split("/")[1];
                String url = "http://localhost:8083/" + determiner;

                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.set("Authorization", authHeader);
                HttpEntity<Boolean> httpRequest = new HttpEntity<>(httpHeaders);

                restTemplate.postForObject(url, httpRequest, Boolean.class);
            }
            return chain.filter(exchange);
        }, 1);
    }

    public static class Config {
        private String test;
    }
}