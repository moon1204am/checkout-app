package se.kth.checkout.services;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.Map;

@Service
public class Integration {
    private final WebClient webClient;

    public Integration(){
        Dotenv dotenv;
        dotenv = Dotenv.configure().load();
        this.webClient = WebClient.builder()
                .baseUrl("https://api.playground.klarna.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString(dotenv.get("CREDENTIALS").getBytes()))
                .build();
    }
    public Mono<String> createOrder(Object req) {
        return this.webClient.post().uri("/checkout/v3/orders/")
                .bodyValue(req)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> fetchOrder(String orderId) {
        return this.webClient.get()
                .uri("/checkout/v3/orders/" + orderId)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> createOrderByToken(String token, Object body) throws Exception{
        return  this.webClient.post()
                .uri("/customer-token/v1/tokens/"+token+"/order")
                .bodyValue(body)
                .retrieve()
                .onStatus( HttpStatus::isError, clientResponse -> clientResponse.bodyToMono(String.class).map(Exception::new))
                .bodyToMono(String.class);
    }

    public Mono<Map> getOrder(String orderId) {
        return this.webClient.get()
                .uri("/checkout/v3/orders/" + orderId)
                .retrieve()
                .bodyToMono(Map.class);
    }
}

