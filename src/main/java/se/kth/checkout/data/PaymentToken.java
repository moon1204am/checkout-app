package se.kth.checkout.data;

import java.sql.Date;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class PaymentToken {
    private @Id @GeneratedValue Long id;
    private String customerToken;
    private Date paymentDate;

    public PaymentToken() {

    }

    public PaymentToken(String customerToken, Date paymentDate){
        this.customerToken = customerToken;
        this.paymentDate = paymentDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerToken() {
        return customerToken;
    }

    public void setCustomerToken(String customerToken) {
        this.customerToken = customerToken;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    @Override
    public boolean equals(Object o){
        if (this == o)
            return true;
        if (!(o instanceof PaymentToken))
            return false;
        PaymentToken paymentToken = (PaymentToken) o;
        return Objects.equals(this.id, paymentToken.id) && Objects.equals(this.customerToken, paymentToken.customerToken)
                && Objects.equals(this.paymentDate, paymentToken.paymentDate);
    }

    @Override
    public int hashCode(){
        return Objects.hash(this.id, this.customerToken, this.paymentDate);
    }

    @Override
    public String toString(){
        return "PaymentToken{" + "id=" + this.id + ", customerToken=" + this.customerToken+
                ", paymentDate=" + paymentDate + '\'' + '}';
    }
}
