package se.kth.checkout.data;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class PaymentTokenNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(PaymentTokenNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String employeeNotFoundHandler(PaymentTokenNotFoundException ex) {
        return ex.getMessage();
    }
}