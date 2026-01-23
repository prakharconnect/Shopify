package com.Prakhar.EccomPayment.Controller;

import com.Prakhar.EccomPayment.DTO.OrderRequest;
import com.Prakhar.EccomPayment.DTO.PaymentResponse;
import com.Prakhar.EccomPayment.DTO.PaymentUpdateDTO;
import com.Prakhar.EccomPayment.Service.PaymentService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/payment")
@CrossOrigin
public class PaymentController {

     @Autowired
    private  PaymentService paymentService;

    @PostMapping("/create")
    public PaymentResponse createOrder(@RequestBody OrderRequest req) throws Exception {
        return paymentService.createOrder(req);
    }

    @PostMapping("/verify")
    public void verifyPayment(@RequestBody PaymentUpdateDTO dto) throws MessagingException {
        paymentService.verifyPayment(
                dto.getOrderId(),
                dto.getPaymentId(),
                dto.getEmail()
        );
    }
}

