 package com.example.demo.repository;

import com.example.demo.model.Order;
import com.example.demo.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // "My Orders" page ke liye: User ke saare orders (Newest first)
    List<Order> findByUserOrderByOrderDateDesc(UserModel user);
}
