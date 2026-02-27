 package com.example.demo.repository;

import com.example.demo.model.Cart;
import com.example.demo.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    // UserModel use kiya hai kyunki entity wahi hai
    Optional<Cart> findByUser(UserModel user);
}