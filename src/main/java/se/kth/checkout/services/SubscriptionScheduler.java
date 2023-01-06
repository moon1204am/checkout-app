package se.kth.checkout.services;


import se.kth.checkout.data.PaymentToken;
import se.kth.checkout.data.PaymentTokenRepository;
import net.minidev.json.JSONArray;
import org.springframework.scheduling.annotation.Scheduled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class SubscriptionScheduler {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionScheduler.class);
    private final PaymentTokenRepository repository;
    private final Integration klarna;

    SubscriptionScheduler(PaymentTokenRepository repository){
        this.repository = repository;
        this.klarna = new Integration();
    }

    @Scheduled(cron = "0 0 2 * * *")
    private void makePayment(){
        LocalDate date = LocalDate.now();
        logger.info(date + ": Fetching today's payments");
        List<PaymentToken> paymentTokens = repository.findByPaymentDate(Date.valueOf(date));
        JSONObject json = getJSON();

        for(PaymentToken paymentToken : paymentTokens){
            String token = paymentToken.getCustomerToken();

            try {
                String result = klarna.createOrderByToken(token,json).block();
                logger.info(date+ ": Order result for Token "+token+ " = "+result);
                paymentToken.setPaymentDate(Date.valueOf(date.plusMonths(1)));
                repository.save(paymentToken);
            }catch (Exception e){
                logger.error(date+ ": Unable to make payment to "+token +" Error message:" + e.getMessage());
            }
        }

        if(paymentTokens.isEmpty()){
            logger.info(date + ": No payments today.");
        } else {
            logger.info(date + ": Today's payments complete.");
        }
    }

    private JSONObject getJSON(){
        JSONObject json = new JSONObject();
        json.put("locale","sv-SE");
        json.put("auto-capture", true);
        json.put("purchase_currency", "SEK");
        json.put("order_amount", 50000);
        json.put("order_tax_amount", 4545);

        JSONArray ar = new JSONArray();
        JSONObject ob = new JSONObject();
        ob.put("type", "digital");
        ob.put("reference", "subscription");
        ob.put("name", "payment for subscription, recurring");
        ob.put("quantity", 5);
        ob.put("quantity_unit", "pcs");
        ob.put("unit_price", 10000);
        ob.put("tax_rate", 1000);
        ob.put("total_amount", 50000);
        ob.put("total_discount_amount", 0);
        ob.put("total_tax_amount",4545);

        ar.add(ob);
        json.put("order_lines", ar);
        return json;
    }

}
