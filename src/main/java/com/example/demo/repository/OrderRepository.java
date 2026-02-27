 package com.example.demo.repository;

import com.example.demo.model.Order;
import com.example.demo.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // "My Orders" page ke liye: User ke saare orders (Newest first)
    List<Order> findByUserOrderByOrderDateDesc(UserModel user);
    
 // 🛡️ VERIFIED PURCHASE CHECK
    // Check karega:
    // 1. Order User ka hona chahiye
    // 2. Order Status 'DELIVERED' hona chahiye
    // 3. OrderItems me wo Product ID hona chahiye
    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END FROM Order o " +
           "JOIN o.orderItems oi " +
           "WHERE o.user.id = :userId " +
           "AND oi.product.id = :productId " +
           "AND o.orderStatus = 'DELIVERED'")
    boolean hasUserBoughtProduct(@Param("userId") Long userId, @Param("productId") Long productId);
}
