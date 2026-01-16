 package com.example.demo.service;

import com.example.demo.model.Address;
import com.example.demo.model.UserModel;
import com.example.demo.repository.AddressRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    // 1. Add New Address
    public Address saveAddress(String email, Address address) {
        // User dhundo
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Address ko User ke sath link karo
        address.setUser(user);

        // Save karo
        return addressRepository.save(address);
    }

    // 2. Get All Addresses for User
    public List<Address> getUserAddresses(String email) {
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return addressRepository.findByUser(user);
    }

    // 3. Delete Address (Optional: Future use ke liye)
    public void deleteAddress(Long addressId) {
        addressRepository.deleteById(addressId);
    }
}