package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort; // ✅ Import Added for Sorting
import org.springframework.stereotype.Service;
import com.example.demo.service.EmailService;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartService cartService; // Cart clear karne ke liye

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VariantRepository variantRepository; // Stock ghatane ke liye
    
  
    @Autowired
    private EmailService emailService;

    // --- MAIN METHOD: PLACE ORDER ---
    @Transactional // Ye bahut zaroori hai! (Sab hoga ya kuch nahi hoga)
    public Order placeOrder(String email, Long addressId, String paymentMethod) {

        // 1. User aur Address dhundo
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        // 2. User ka Cart nikalo
     // .orElse(null) ka matlab: Agar cart mile to dedo, nahi to null dedo
        Cart cart = cartRepository.findByUser(user).orElse(null);
        if (cart == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // 3. Naya Order Object banao
        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        order.setPaymentMethod(paymentMethod);
        order.setOrderStatus("PENDING"); // Shuruaat mein Pending
        
        // 4. Cart Items ko Order Items mein convert karo
        double totalAmount = 0.0;

        for (CartItem cartItem : cart.getItems()) {
            Variant variant = cartItem.getVariant();
            int qty = cartItem.getQuantity();

            // --- STOCK CHECK & UPDATE ---
            if (variant.getStock() < qty) {
                throw new RuntimeException("Out of Stock: " + cartItem.getProduct().getName());
            }
            
            // Stock ghatao (Inventory Management)
            variant.setStock(variant.getStock() - qty);
            variantRepository.save(variant); 

            // Order Item banao (Price Freeze Logic)
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setVariant(variant);
            orderItem.setQuantity(qty);
            orderItem.setOrderedPrice(variant.getPrice()); // Aaj ki price save karo

            // Order list mein add karo
            order.getOrderItems().add(orderItem);

            // Total calculate karo
            totalAmount += (variant.getPrice() * qty);
        }

        order.setTotalAmount(totalAmount);

        // 5. Order Save karo (Database me entry)
        Order savedOrder = orderRepository.save(order);

        // 6. Cart Khali karo (Kyunki order ho gaya)
        cartService.clearCart(email); 
        
     // 🔴 NEW STEP: 7. Send Email Notification (Async tarike se socho)
        // Hum ise try-catch mein rakhenge taaki agar email fail ho, to bhi order cancel na ho
        try {
            emailService.sendOrderConfirmation(email, savedOrder.getId(), savedOrder.getTotalAmount());
        } catch (Exception e) {
             System.err.println("Email sending failed, but order is placed: " + e.getMessage());
        }

        return savedOrder;
    }
    
    // Helper: User ke orders dekhne ke liye
    public List<Order> getUserOrders(String email) {
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }
    
    @Transactional // Transaction zaroori hai (Stock update + Order update)
    public Order cancelOrder(Long orderId) {
        // 1. Order dhundo
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // 2. Check: Kya Order pehle se Shipped/Delivered hai?
        if (!"PENDING".equals(order.getOrderStatus())) {
            throw new RuntimeException("Only PENDING orders can be cancelled.");
        }

        // 3. Status change karo
        order.setOrderStatus("CANCELLED");

        // 4. ✅ STOCK RESTORE LOGIC (Wapas jod do)
        for (OrderItem item : order.getOrderItems()) {
            Variant variant = item.getVariant();
            // Current Stock + Cancelled Quantity
            variant.setStock(variant.getStock() + item.getQuantity());
            variantRepository.save(variant);
        }
         
        return orderRepository.save(order);
    }

    // ==========================================
    // 🚀 NEW: ADMIN METHODS (For Order Lifecycle)
    // ==========================================

    // 1. Get All Orders (Newest First)
    public List<Order> getAllOrders() {
        // Sort.by(Sort.Direction.DESC, "orderDate") use kar rahe hain taaki naye order upar dikhe
        return orderRepository.findAll(Sort.by(Sort.Direction.DESC, "orderDate"));
    }

    // 2. Update Order Status (Pending -> Shipped -> Delivered)
    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        order.setOrderStatus(status);
        return orderRepository.save(order);
    }
}
















//update this code in upper part jisse admin ko sare order miljayenge 
// package com.example.demo.service;
//
//import com.example.demo.model.*;
//import com.example.demo.repository.*;
//import jakarta.transaction.Transactional;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import com.example.demo.service.EmailService;
//
//import java.util.List;
//
//@Service
//public class OrderService {
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//    @Autowired
//    private OrderItemRepository orderItemRepository;
//
//    @Autowired
//    private CartRepository cartRepository;
//
//    @Autowired
//    private CartService cartService; // Cart clear karne ke liye
//
//    @Autowired
//    private AddressRepository addressRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private VariantRepository variantRepository; // Stock ghatane ke liye
//    
//  
//    @Autowired
//    private EmailService emailService;
//
//    // --- MAIN METHOD: PLACE ORDER ---
//    @Transactional // Ye bahut zaroori hai! (Sab hoga ya kuch nahi hoga)
//    public Order placeOrder(String email, Long addressId, String paymentMethod) {
//
//        // 1. User aur Address dhundo
//        UserModel user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Address address = addressRepository.findById(addressId)
//                .orElseThrow(() -> new RuntimeException("Address not found"));
//
//        // 2. User ka Cart nikalo
//     // .orElse(null) ka matlab: Agar cart mile to dedo, nahi to null dedo
//        Cart cart = cartRepository.findByUser(user).orElse(null);
//        if (cart == null || cart.getItems().isEmpty()) {
//            throw new RuntimeException("Cart is empty");
//        }
//
//        // 3. Naya Order Object banao
//        Order order = new Order();
//        order.setUser(user);
//        order.setAddress(address);
//        order.setPaymentMethod(paymentMethod);
//        order.setOrderStatus("PENDING"); // Shuruaat mein Pending
//        
//        // 4. Cart Items ko Order Items mein convert karo
//        double totalAmount = 0.0;
//
//        for (CartItem cartItem : cart.getItems()) {
//            Variant variant = cartItem.getVariant();
//            int qty = cartItem.getQuantity();
//
//            // --- STOCK CHECK & UPDATE ---
//            if (variant.getStock() < qty) {
//                throw new RuntimeException("Out of Stock: " + cartItem.getProduct().getName());
//            }
//            
//            // Stock ghatao (Inventory Management)
//            variant.setStock(variant.getStock() - qty);
//            variantRepository.save(variant); 
//
//            // Order Item banao (Price Freeze Logic)
//            OrderItem orderItem = new OrderItem();
//            orderItem.setOrder(order);
//            orderItem.setProduct(cartItem.getProduct());
//            orderItem.setVariant(variant);
//            orderItem.setQuantity(qty);
//            orderItem.setOrderedPrice(variant.getPrice()); // Aaj ki price save karo
//
//            // Order list mein add karo
//            order.getOrderItems().add(orderItem);
//
//            // Total calculate karo
//            totalAmount += (variant.getPrice() * qty);
//        }
//
//        order.setTotalAmount(totalAmount);
//
//        // 5. Order Save karo (Database me entry)
//        Order savedOrder = orderRepository.save(order);
//
//        // 6. Cart Khali karo (Kyunki order ho gaya)
//        cartService.clearCart(email); 
//        
//     // 🔴 NEW STEP: 7. Send Email Notification (Async tarike se socho)
//        // Hum ise try-catch mein rakhenge taaki agar email fail ho, to bhi order cancel na ho
//        try {
//            emailService.sendOrderConfirmation(email, savedOrder.getId(), savedOrder.getTotalAmount());
//        } catch (Exception e) {
//             System.err.println("Email sending failed, but order is placed: " + e.getMessage());
//        }
//
//        return savedOrder;
//    }
//    
//    // Helper: User ke orders dekhne ke liye
//    public List<Order> getUserOrders(String email) {
//        UserModel user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        return orderRepository.findByUserOrderByOrderDateDesc(user);
//    }
//    
//    @Transactional // Transaction zaroori hai (Stock update + Order update)
//    public Order cancelOrder(Long orderId) {
//        // 1. Order dhundo
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new RuntimeException("Order not found"));
//
//        // 2. Check: Kya Order pehle se Shipped/Delivered hai?
//        if (!"PENDING".equals(order.getOrderStatus())) {
//            throw new RuntimeException("Only PENDING orders can be cancelled.");
//        }
//
//        // 3. Status change karo
//        order.setOrderStatus("CANCELLED");
//
//        // 4. ✅ STOCK RESTORE LOGIC (Wapas jod do)
//        for (OrderItem item : order.getOrderItems()) {
//            Variant variant = item.getVariant();
//            // Current Stock + Cancelled Quantity
//            variant.setStock(variant.getStock() + item.getQuantity());
//            variantRepository.save(variant);
//        }
//         
//        return orderRepository.save(order);
//    }
//}
