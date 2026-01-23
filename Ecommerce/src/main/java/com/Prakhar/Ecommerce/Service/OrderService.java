package com.Prakhar.Ecommerce.Service;


import com.Prakhar.Ecommerce.DTO.OrderDto;
import com.Prakhar.Ecommerce.DTO.OrderItemDto;
import com.Prakhar.Ecommerce.Entity.OrderItems;
import com.Prakhar.Ecommerce.Entity.Orders;
import com.Prakhar.Ecommerce.Entity.Product;
import com.Prakhar.Ecommerce.Entity.User;
import com.Prakhar.Ecommerce.repo.OrderRepository;
import com.Prakhar.Ecommerce.repo.ProductRepository;
import com.Prakhar.Ecommerce.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public OrderDto placeOrder(Long userId, Map<Long,Integer>productQuantities,double totalAmount)
    {

        User user = userRepository.findById(userId)
                .orElseThrow(()->new RuntimeException("User Not found"));

        Orders orders=new Orders();
        orders.setUser(user);
        orders.setOrderDate(new Date());
        orders.setStatus("Pending");
        orders.setTotalAmount(totalAmount);

        List<OrderItems> orderItems=new ArrayList<>();
        List<OrderItemDto> orderItemDtos=new ArrayList<>();

        for(Map.Entry<Long,Integer> entry:productQuantities.entrySet())
        {
            Product product=productRepository.findById(entry.getKey())
                    .orElseThrow(()->new RuntimeException("Product does not found"));

            OrderItems orderItems1= new OrderItems();
            orderItems1.setOrders(orders);
            orderItems1.setProduct(product);
            orderItems1.setQuantity(entry.getValue());
            orderItems.add(orderItems1);
            orderItemDtos.add(new OrderItemDto(product.getName(),product.getPrice(), entry.getValue()));
        }

        orders.setOrderItems(orderItems);

        Orders saveOrders=orderRepository.save(orders);

        return new OrderDto(saveOrders.getId(),saveOrders.getTotalAmount(),saveOrders.getStatus(),
                saveOrders.getOrderDate(),orderItemDtos);


    }


        public List<OrderDto>getAllOrders()
        {
             List<Orders> orders=orderRepository.findAllOrdersWithUsers();
             return orders.stream().map(this::convertToDto).collect(Collectors.toList());
        }

    private OrderDto convertToDto(Orders orders) {

         List<OrderItemDto> orderItems= orders.getOrderItems().stream()
                 .map(item->new OrderItemDto(
                         item.getProduct().getName(),
                         item.getProduct().getPrice(),
                         item.getQuantity())).collect(Collectors.toList());

           return new OrderDto(
                   orders.getId(),
                   orders.getTotalAmount(),
                   orders.getStatus(),
                   orders.getOrderDate(),
                   orders.getUser()!=null ? orders.getUser().getName() :"Unknown",
                   orders.getUser()!=null ? orders.getUser().getEmail():"Unknown",
                   orderItems
           );


    }

      public List<OrderDto> getOrderByUser(Long userId)
      {
          Optional<User> userop=userRepository.findById(userId);

          if(userop.isEmpty())
          {
              throw new RuntimeException("User Not find there");

          }

           User user= userop.get();
          List<Orders> ordersList=orderRepository.findByUser(user);
          return ordersList.stream().map(this::convertToDto).collect(Collectors.toList());

      }

    public void createPendingOrder(String razorpayOrderId, Long amount, String email) {

        Orders order = new Orders();
        order.setRazorpayOrderId(razorpayOrderId);
        order.setTotalAmount(amount);
        order.setEmail(email);
        order.setStatus("PENDING");
        order.setOrderDate(new Date());

        orderRepository.save(order);
    }


    public void updatePaymentSuccess(String razorpayOrderId, String paymentId) {

        Orders order = orderRepository.findByRazorpayOrderId(razorpayOrderId);

        if(order == null){
            throw new RuntimeException("Order not found!");
        }

        order.setPaymentId(paymentId);
        order.setStatus("SUCCESS");

        orderRepository.save(order);
    }

}


