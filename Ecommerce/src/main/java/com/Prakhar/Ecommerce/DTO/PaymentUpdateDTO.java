package com.Prakhar.Ecommerce.DTO;





public class PaymentUpdateDTO {
    private String orderId;
    private String paymentId;
    private Long amount;
    private String email;

    public PaymentUpdateDTO() {}

    public PaymentUpdateDTO(String orderId, String paymentId, Long amount, String email) {
        this.orderId = orderId;
        this.paymentId = paymentId;
        this.amount = amount;
        this.email = email;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
