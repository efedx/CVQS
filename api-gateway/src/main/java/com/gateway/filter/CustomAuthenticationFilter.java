package com.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.exceptions.SecurityExceptionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class CustomAuthenticationFilter extends AbstractGatewayFilterFactory<CustomAuthenticationFilter.Config> {

    @Value("url.security")
    String securityUrl;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private WebClient webClient;
    @Autowired
    private ObjectMapper objectMapper;
    public CustomAuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {

        return new OrderedGatewayFilter((exchange, chain) -> {

            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                exchange.getResponse().setStatusCode(HttpStatusCode.valueOf(400));
                return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                        .bufferFactory().wrap("No authorization header".getBytes())));
            }


            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

            String contextPath = exchange.getRequest().getPath().value();
            String determiner = contextPath.split("/")[1];
            String url = "http://SECURITY:8085/" + determiner;

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Authorization", authHeader);
            HttpEntity<Boolean> httpRequest = new HttpEntity<>(httpHeaders);

            try {
                Boolean value = restTemplate.postForObject(url, httpRequest, Boolean.class);
            }
            catch (HttpClientErrorException e) {

                if(!e.getResponseBodyAsString().isEmpty()) {

                    SecurityExceptionResponse securityExceptionResponse = null;

                    try {
                        securityExceptionResponse = objectMapper.readValue(e.getResponseBodyAsString(), SecurityExceptionResponse.class);
                    } catch (JsonProcessingException ex) {
                        throw new RuntimeException(ex);
                    }
                    ResponseEntity<SecurityExceptionResponse> responseEntity = ResponseEntity
                            .status(e.getStatusCode())
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(securityExceptionResponse);
                    exchange.getResponse().setStatusCode(e.getStatusCode());

                    return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                            .bufferFactory().wrap(securityExceptionResponse.message().getBytes())));
                }
                else {
                    ResponseEntity<SecurityExceptionResponse> responseEntity = ResponseEntity
                            .status(e.getStatusCode())
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(null);
                    exchange.getResponse().setStatusCode(e.getStatusCode());

                    return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                            .bufferFactory().wrap("No authorization".getBytes())));
                }
            }
            return chain.filter(exchange);
        }, 1);
    }

    public static class Config {
        private String test;
    }
}