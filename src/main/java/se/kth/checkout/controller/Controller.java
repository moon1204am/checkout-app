package se.kth.checkout.controller;

import se.kth.checkout.services.Integration;
import se.kth.checkout.data.PaymentToken;
import se.kth.checkout.data.PaymentTokenRepository;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import org.json.JSONObject;

import java.sql.Date;

import java.time.LocalDate;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class Controller {
    private Integration integration;
    private final PaymentTokenRepository repository;

    Controller(PaymentTokenRepository repository){
        this.integration = new Integration();
        this.repository = repository;
    }

    @CrossOrigin(origins = "https://localhost:3000")
    @PostMapping("/order")
    public Mono<String> newOrder(@RequestBody Object req){

        return this.integration.createOrder(req);
    }

    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping("/order/{orderId}")
    public Mono<String> getOrder(@PathVariable(value="orderId") String id){
        return this.integration.fetchOrder(id);
    }

    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping("/checkout/{orderId}")
    public Object checkoutOrder(@PathVariable(value="orderId") String id){


        String result = this.integration.fetchOrder(id).block();

        if(result != null && result.contains("recurring_token")){
            PaymentToken paymentToken = new PaymentToken();
            JSONObject json = new JSONObject(result);
            paymentToken.setCustomerToken(json.getString("recurring_token"));
            LocalDate today = LocalDate.now();
            LocalDate date = today.plusMonths(1);
            paymentToken.setPaymentDate(Date.valueOf(date));

            repository.save(paymentToken);
        }

        return (Object) result;

    }
}
