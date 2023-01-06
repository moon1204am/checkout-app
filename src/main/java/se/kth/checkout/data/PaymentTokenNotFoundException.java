package se.kth.checkout.data;


public class PaymentTokenNotFoundException extends RuntimeException {
    public PaymentTokenNotFoundException(Long id) {
        super("Could not find payment " + id);
    }
}