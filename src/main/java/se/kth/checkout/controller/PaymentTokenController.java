package se.kth.checkout.controller;

import java.util.List;
import java.util.stream.Collectors;
import se.kth.checkout.data.PaymentToken;
import se.kth.checkout.data.PaymentTokenNotFoundException;
import se.kth.checkout.data.PaymentTokenRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.hateoas.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class PaymentTokenController {

    private final PaymentTokenRepository repository;

    PaymentTokenController(PaymentTokenRepository repository) {
        this.repository = repository;
    }

    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/paymentTokens")
    CollectionModel<EntityModel<PaymentToken>> all(){
        List<EntityModel<PaymentToken>> paymentTokens = repository.findAll().stream()
                .map( paymentToken -> EntityModel.of(paymentToken,
                        linkTo(methodOn(PaymentTokenController.class).one(paymentToken.getId())).withSelfRel(),
                        linkTo(methodOn(PaymentTokenController.class).all()).withRel("paymentTokens")))
                .collect(Collectors.toList());

        return CollectionModel.of(paymentTokens,
                linkTo(methodOn(PaymentTokenController.class).all()).withSelfRel());
    }
    // end::get-aggregate-root[]

    @PostMapping("/paymentTokens")
    PaymentToken newPaymentToken(@RequestBody PaymentToken newPaymentToken) {
        return repository.save(newPaymentToken);
    }

    // Single item

    @GetMapping("/paymentTokens/{id}")
    EntityModel<PaymentToken> one(@PathVariable Long id) {
        PaymentToken paymentToken = repository.findById(id)
                .orElseThrow(() -> new PaymentTokenNotFoundException(id));

        return EntityModel.of(paymentToken, //
                linkTo(methodOn(PaymentTokenController.class).one(id)).withSelfRel(),
                linkTo(methodOn(PaymentTokenController.class).all()).withRel("paymentTokens"));
    }

    @PutMapping("/paymentTokens/{id}")
    PaymentToken replacePaymentToken(@RequestBody PaymentToken newPaymentToken, @PathVariable Long id) {

        return repository.findById(id)
                .map(PaymentToken -> {
                    PaymentToken.setCustomerToken(newPaymentToken.getCustomerToken());
                    PaymentToken.setPaymentDate(newPaymentToken.getPaymentDate());
                    return repository.save(PaymentToken);
                })
                .orElseGet(() -> {
                    newPaymentToken.setId(id);
                    return repository.save(newPaymentToken);
                });
    }

    @DeleteMapping("/paymentTokens/{id}")
    void deletePaymentToken(@PathVariable Long id) {
        repository.deleteById(id);
    }
}