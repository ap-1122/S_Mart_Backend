package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private VariantRepository variantRepository;

    // 1. Get Cart Logic (Agar cart nahi hai, to naya banao)
    public Cart getCart(String email) {
        // Tumhare UserRepository code ke hisab se 'findByEmail' use kiya hai
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // User ka cart dhundo, agar nahi mila to naya create karo
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart(user);
                    return cartRepository.save(newCart);
                });
    }

    // 2. Add Item to Cart Logic
    public Cart addToCart(String email, Long productId, Long variantId, int quantity) {
        // Step A: User ka Cart nikalo
        Cart cart = getCart(email);

        // Step B: Product aur Variant verify karo
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Variant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new RuntimeException("Variant not found"));

        // Step C: Check karo ki kya ye Variant pehle se Cart me hai?
        CartItem existingItem = null;

        for (CartItem item : cart.getItems()) {
            // Check matching Variant ID
            if (item.getVariant().getId().equals(variantId)) {
                existingItem = item;
                break;
            }
        }

        if (existingItem != null) {
            // Case 1: Item pehle se hai -> Quantity badha do
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            // Case 2: Naya Item hai -> New CartItem create karo
            CartItem newItem = new CartItem(cart, product, variant, quantity);
            cart.getItems().add(newItem);
        }

        // Step D: Cart Total Recalculate karo
        calculateCartTotal(cart);

        // Step E: Save and Return
        return cartRepository.save(cart);
    }

    // 3. Update Quantity (Plus/Minus ke liye)
    public Cart updateQuantity(String email, Long cartItemId, int quantity) {
        Cart cart = getCart(email);

        Optional<CartItem> itemOpt = cart.getItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst();

        if (itemOpt.isPresent()) {
            CartItem item = itemOpt.get();
            item.setQuantity(quantity); 
            calculateCartTotal(cart);
            return cartRepository.save(cart);
        }
        
        throw new RuntimeException("Cart Item not found in user's cart");
    }

    // 4. Remove Item Logic
    public Cart removeFromCart(String email, Long cartItemId) {
        Cart cart = getCart(email);

        // List se item remove karo
        boolean removed = cart.getItems().removeIf(item -> item.getId().equals(cartItemId));
        
        if (removed) {
            calculateCartTotal(cart);
            return cartRepository.save(cart);
        }

        throw new RuntimeException("Item not found in cart");
    }

    // Helper: Total Price Calculation
    private void calculateCartTotal(Cart cart) {
        double total = 0.0;
        for (CartItem item : cart.getItems()) {
            // CartItem model me getSubTotal() method hona chahiye jo hamne banaya tha
            total += item.getSubTotal();
        }
        cart.setTotalAmount(total);
    }
    
 // Order hone ke baad cart saaf karne ke liye
    public void clearCart(String email) {
        Cart cart = getCart(email);
        cart.getItems().clear(); // List khali karo
        cart.setTotalAmount(0.0); // Total zero karo
        cartRepository.save(cart); // Update DB
    }
}