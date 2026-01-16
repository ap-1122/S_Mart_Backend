 package com.example.demo.controller;

import com.example.demo.model.Address;
import com.example.demo.model.UserModel;
import com.example.demo.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@CrossOrigin(origins = "http://localhost:5173") // React URL
public class AddressController {

    @Autowired
    private AddressService addressService;

    // Helper: Email extractor (Wahi jo CartController me use kiya tha)
    private String getEmailFromAuth(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserModel) {
            return ((UserModel) principal).getEmail();
        }
        return principal.toString();
    }

    // 1. Add Address
    // URL: http://localhost:8080/api/addresses/add
    @PostMapping("/add")
    public ResponseEntity<Address> addAddress(@RequestBody Address address, Authentication authentication) {
        String email = getEmailFromAuth(authentication);
        Address savedAddress = addressService.saveAddress(email, address);
        return ResponseEntity.ok(savedAddress);
    }

    // 2. Get My Addresses
    // URL: http://localhost:8080/api/addresses/
    @GetMapping("/")
    public ResponseEntity<List<Address>> getMyAddresses(Authentication authentication) {
        String email = getEmailFromAuth(authentication);
        List<Address> addresses = addressService.getUserAddresses(email);
        return ResponseEntity.ok(addresses);
    }

    // 3. Delete Address
    // URL: http://localhost:8080/api/addresses/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return ResponseEntity.ok("Address deleted successfully");
    }
}