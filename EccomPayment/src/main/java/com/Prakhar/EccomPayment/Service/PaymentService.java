package com.Prakhar.EccomPayment.Service;

import com.Prakhar.EccomPayment.DTO.*;

import com.razorpay.RazorpayClient;
import com.razorpay.Order;
import jakarta.mail.MessagingException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentService {

    @Autowired
    private  RestTemplate restTemplate;
    @Autowired
    private EmailService emailService;

    private Long lastAmount;

    @Value("${ecommerce.service.url}")
    private String ecommerceServiceUrl;


    @Value("${razorpay.keyId}")
    private String key;

    @Value("${razorpay.keySecret}")
    private String secret;

    public PaymentResponse createOrder(OrderRequest req) throws Exception {

        RazorpayClient client = new RazorpayClient(key, secret);



        int totalAmount = calculateAmount(req);

        JSONObject options = new JSONObject();
        options.put("amount", Math.round(totalAmount * 100));
        options.put("currency", "INR");

        Order razorOrder = client.orders.create(options);

        PaymentUpdateDTO dto = new PaymentUpdateDTO(
                razorOrder.get("id").toString(),
                null,
                (long) totalAmount * 100,
                req.getEmail(),
                req.getItems()
        );


        restTemplate.postForObject(
                ecommerceServiceUrl + "/orders/pending",
                new PaymentUpdateDTO(
                        razorOrder.get("id").toString(),
                        null,
                        (long) totalAmount * 100,
                        req.getEmail(),
                        req.getItems()
                ),
                Void.class
        );
        lastAmount = (long) totalAmount * 100;
        PaymentResponse res = new PaymentResponse();
        res.setRazorpayOrderId(razorOrder.get("id"));
        res.setKey(key);
        res.setAmount((long) totalAmount * 100);

        return res;
    }

    private int calculateAmount(OrderRequest req) {
        int total = 0;
        for (OrderItemRequest item : req.getItems()) {
            total += item.getQuantity() * item.getPrice();

        }
        return total;
    }
    public void verifyPayment(String razorOrderId, String paymentId, String email) throws MessagingException {

        OrderDetailsDTO savedOrder =
                restTemplate.getForObject(
                        ecommerceServiceUrl+"/orders/by-razorpay/" + razorOrderId,
                        OrderDetailsDTO.class
                );

        restTemplate.postForObject(
                ecommerceServiceUrl+"/orders/success",
                new PaymentUpdateDTO(
                        razorOrderId,
                        paymentId,
                        null,
                        email
                ),

                Void.class
        );

        System.out.println("MAIL CODE REACHED");
        try {
            emailService.sendOrderEmail(
                    email,
                    lastAmount / 100,
                    paymentId,
                    razorOrderId
            );
            System.out.println("EMAIL SENT SUCCESS");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
