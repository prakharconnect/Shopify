package com.Prakhar.Ecommerce.repo;

import com.Prakhar.Ecommerce.Entity.Orders;
import com.Prakhar.Ecommerce.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders,Long> {


          @Query("SELECT o from Orders o JOIN FETCH o.user")
          List<Orders> findAllOrdersWithUsers();


         List<Orders>findByUser(User user);

         List<Orders> findByEmail(String email);

         List<Orders> findByStatus(String status);

           Orders findByRazorpayOrderId(String razorpayOrderId);
           Orders findTopByOrderByIdDesc();

          long countByStatus(String status);

          Orders findTopByStatusOrderByIdDesc(String status);

}
