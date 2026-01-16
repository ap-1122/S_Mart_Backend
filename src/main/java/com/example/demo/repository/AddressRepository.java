 package com.example.demo.repository;

import com.example.demo.model.Address;
import com.example.demo.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    
    // SQL Logic: SELECT * FROM addresses WHERE user_id = ?
    List<Address> findByUser(UserModel user);
}