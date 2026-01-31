 package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Autowired
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                
                // 1. Pre-flight requests
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                
                // 2. PUBLIC ENDPOINTS
                .requestMatchers("/api/auth/**", "/auth/**").permitAll()

                // --- PUBLIC GET ACCESS ---
                // Note: "/api/products" AND "/api/products/**" dono zaroori hain
                .requestMatchers(HttpMethod.GET, "/api/products", "/api/products/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/categories", "/api/categories/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/brands", "/api/brands/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/variants", "/api/variants/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/attributes", "/api/attributes/**").permitAll()
                
                // 3. USER SECURED
                .requestMatchers("/api/cart/**").authenticated()
                .requestMatchers("/api/orders/**").authenticated()
                .requestMatchers("/api/users/**").authenticated()
                .requestMatchers("/api/addresses/**").authenticated()
                
                // 4. ADMIN & SELLER (CREATE/UPDATE/DELETE)
                // Yahan humne path matching strong kar di hai
                
                // Products
                .requestMatchers(HttpMethod.POST, "/api/products", "/api/products/**").hasAnyAuthority("ADMIN", "SELLER")
                .requestMatchers(HttpMethod.PUT, "/api/products", "/api/products/**").hasAnyAuthority("ADMIN", "SELLER")
                .requestMatchers(HttpMethod.DELETE, "/api/products", "/api/products/**").hasAnyAuthority("ADMIN", "SELLER")
                
                // Images
                .requestMatchers("/api/product-images/**").hasAnyAuthority("ADMIN", "SELLER")

                // Variants
                .requestMatchers(HttpMethod.POST, "/api/variants", "/api/variants/**").hasAnyAuthority("ADMIN", "SELLER")
                
                // Categories & Brands (Admin Only)
                .requestMatchers(HttpMethod.POST, "/api/categories", "/api/categories/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/categories", "/api/categories/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/categories", "/api/categories/**").hasAuthority("ADMIN")

                .requestMatchers(HttpMethod.POST, "/api/brands", "/api/brands/**").hasAuthority("ADMIN")
                
                // Attributes
                .requestMatchers(HttpMethod.POST, "/api/attributes", "/api/attributes/**").hasAuthority("ADMIN")
                
                // Webhook
                .requestMatchers(HttpMethod.POST, "/api/payment/webhook").permitAll()
                
                // Admin User Mgmt
                .requestMatchers("/api/users/all").hasAuthority("ADMIN")
                .requestMatchers("/api/users/*/role").hasAuthority("ADMIN")
                
                // 5. CATCH ALL
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173")); 
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}




// baki sab sahi hai per kuch dikkat hai jisse product add nahi ho pa raha 

//package com.example.demo.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import java.util.Arrays;
//
//@Configuration
//public class SecurityConfig {
//
//    private final JwtFilter jwtFilter;
//
//    @Autowired
//    public SecurityConfig(JwtFilter jwtFilter) {
//        this.jwtFilter = jwtFilter;
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//            .csrf(csrf -> csrf.disable())
//            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//            .authorizeHttpRequests(auth -> auth
//                
//                // 1. Pre-flight requests (OPTIONS) allow (Important for React)
//                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                
//                // 2. PUBLIC ENDPOINTS (No Login Required)
//                .requestMatchers("/api/auth/**", "/auth/**").permitAll() // Login/Register
//
//                // --- PUBLIC VIEW ACCESS (GET Only) ---
//                // Anyone can view products, categories, brands, variants, attributes
//                .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
//                .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
//                .requestMatchers(HttpMethod.GET, "/api/brands/**").permitAll()
//                .requestMatchers(HttpMethod.GET, "/api/variants/**").permitAll()
//                .requestMatchers(HttpMethod.GET, "/api/attributes/**").permitAll()
//                
//                // 3. USER SECURED ENDPOINTS (Login Required)
//                .requestMatchers("/api/cart/**").authenticated()      // Cart Operations
//                .requestMatchers("/api/orders/**").authenticated()    // Place/View Orders
//                .requestMatchers("/api/users/**").authenticated()     // User Profile
//                .requestMatchers("/api/addresses/**").authenticated() // Address Management
//                
//                // 4. ADMIN & SELLER SECURED ENDPOINTS (Updated)
//                
//                // Product Management (Create/Update/Delete) -> ADMIN & SELLER
//                .requestMatchers(HttpMethod.POST, "/api/products/**").hasAnyAuthority("ADMIN", "SELLER")
//                .requestMatchers(HttpMethod.PUT, "/api/products/**").hasAnyAuthority("ADMIN", "SELLER")
//                .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasAnyAuthority("ADMIN", "SELLER")
//                
//                // Category & Brand Management -> ADMIN ONLY
//                .requestMatchers(HttpMethod.POST, "/api/categories/**").hasAuthority("ADMIN")
//                .requestMatchers(HttpMethod.POST, "/api/brands/**").hasAuthority("ADMIN")
//                
//                // Variant Management -> ADMIN & SELLER
//                .requestMatchers(HttpMethod.POST, "/api/variants/**").hasAnyAuthority("ADMIN", "SELLER")
//                
//                // Attribute Management -> ADMIN ONLY
//                .requestMatchers(HttpMethod.POST, "/api/attributes/**").hasAuthority("ADMIN")
//                
//                // Image Upload -> ADMIN & SELLER
//                .requestMatchers("/api/product-images/**").hasAnyAuthority("ADMIN", "SELLER")
//                
//                // Future Admin Routes -> ADMIN ONLY
//                .requestMatchers("/api/admin/**").hasAuthority("ADMIN")
//                
//             // ✅ Payment Webhook ko Public karo (Razorpay bina login ke call karega)
//                .requestMatchers(HttpMethod.POST, "/api/payment/webhook").permitAll()
//                
//                
//             // ✅ NEW: Admin User Management
//                .requestMatchers("/api/users/all").hasAuthority("ADMIN")
//                .requestMatchers("/api/users/*/role").hasAuthority("ADMIN")
//                
//                // 5. CATCH ALL
//                .anyRequest().authenticated()
//            )
//            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        // Allow your Frontend URL
//        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173")); 
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "*"));
//        configuration.setAllowCredentials(true);
//        
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
//}










//role based login and permission dene ke liye kuch endpoints ko block kiye like admin and seller page access from user 

//package com.example.demo.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod; // Import Zaroori hai
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import java.util.Arrays;
//
//@Configuration
//public class SecurityConfig {
//
//    private final JwtFilter jwtFilter;
//
//    @Autowired
//    public SecurityConfig(JwtFilter jwtFilter) {
//        this.jwtFilter = jwtFilter;
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//            .csrf(csrf -> csrf.disable())
//            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//            .authorizeHttpRequests(auth -> auth
//            		
//            		
//  //ISS PURE PART KO SIRF TESTING KE LIYE COMMENT KIYAHAI AUR SARE POINT KO ALLOW IN LOWER CODE           		
////                // 1. Pre-flight requests (OPTIONS) ko hamesha allow karo (Fixes 403 on Upload)
////                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
////                
////                // 2. Public endpoints
////                .requestMatchers("/auth/**").permitAll()
////                .requestMatchers("/public/**").permitAll()
////
////                // 3. TESTING MODE: Admin & APIs Open
////                .requestMatchers(
////                    "/admin/**",
////                    "/api/brands/**",
////                    "/api/categories/**",
////                    "/api/products/**",     // Products Get/Post
////                    "/api/variants/**",
////                    "/api/attributes/**",
////                    "/api/product-images/**" // Image Upload
////                ).permitAll()
//            		
//            		
//            		// 2. Secured Endpoints (Cart & Address) - Sirf Logged-in users ke liye
//                    // Future Reference: Yahan hum explicitly bata rahe hain ki in paths par Token zaroori hai
////                    .requestMatchers("/api/cart/**").authenticated()      // Cart Secure
////                    .requestMatchers("/api/addresses/**").authenticated() // Address Secure
//                    
//                    // 3. Admin Endpoints (Future ke liye)
//                    // .requestMatchers("/api/admin/**").hasRole("ADMIN")
//            		
//            		
//            		
////            		// ✅ NEW: Order System (Jo abhi banaya)
////                    .requestMatchers("/api/orders/**").authenticated()    // Order place karna, My Orders dekhna, Cancel karna
////                    
////                    // ✅ NEW: User Profile (Jo abhi banaya)
////                    .requestMatchers("/api/users/**").authenticated()     // Profile dekhna aur update karna
////                    
////                    // ====================================================
////                    // 🔴 3. ADMIN ENDPOINTS (Strictly Admin Only)
////                    // ====================================================
////                    // Future me jab hum /api/admin/products ya /api/admin/orders banayenge
////                    // Note: .hasAuthority("ADMIN") check karega ki Token me role "ADMIN" hai ya nahi
////                    .requestMatchers("/api/admin/**").hasAuthority("ADMIN")
////
////                // 4. Baki sab band
////                .anyRequest().authenticated()
//            		
//            		
//            		
//           //YE SIRF TESTING KE LIYE SARE POIN ALOOW KIYA HAI 
//            		// ⚠️ DEBUG MODE: Sab kuch allow kar do (Taki 403 ka kissa khatam ho)
//                    .requestMatchers("/**").permitAll()
//            )
//            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173")); // React
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        configuration.setAllowedHeaders(Arrays.asList("*")); // Sab headers allow karo
//        configuration.setAllowCredentials(true);
//        
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
//}
//
//



























//package com.example.demo.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import java.util.Arrays;
//
//@Configuration
//public class SecurityConfig {
//
//    private final JwtFilter jwtFilter;
//
//    @Autowired
//    public SecurityConfig(JwtFilter jwtFilter) {
//        this.jwtFilter = jwtFilter;
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // <-- CORS Added Here
//            .csrf(csrf -> csrf.disable())
//            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//            .authorizeHttpRequests(auth -> auth
//                .requestMatchers("/auth/**").permitAll() // Login/Register sabke liye open
//                
//                
//             // --- YE LINE ADD KARO ---
//                .requestMatchers("/admin/**").permitAll() // Admin ke saare kaam (Product add karna) ab open hain
//                // -
//             // 2. ✨ NEW: Public Products aur Categories sabke liye khule (Ye missing tha!)
//                .requestMatchers("/public/**").permitAll() 
//                .requestMatchers("/admin/products/categories").permitAll() // Categories dikhane ke liye
//                .requestMatchers("/admin/products/attributes").permitAll() // Dropdown ke liye
//                
//                
//                
//                // 3. ✨ NEW: Images upload karne ke liye (Admin) - Filhal allow kar rahe hain testing ke liye
//                .requestMatchers("/admin/products/**").permitAll()
//                
//                
//             // 2. ⚠️ TESTING MODE: Admin APIs ko Temporarily Open kar rahe hain
//                // Jab testing poori ho jaye, inhe wapas secure kar denge
//                .requestMatchers(
//                    "/api/brands/**", 
//                    "/api/categories/**", 
//                    "/api/products/**", 
//                    "/api/variants/**", 
//                    "/api/attributes/**",
//                 // ⚠️ YE LINE CHECK KARO (Ye zaroori hai image upload ke liye)
//                    "/api/product-images/**"
//                ).permitAll()
//                
//                .anyRequest().authenticated()
//            )
//            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//
//    // Ye Naya Method hai jo React connection allow karega
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
////        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); javascript url  
//     // CHANGE THIS LINE: 3000 -> 5173  // React ka URL 
//        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
//        configuration.setAllowCredentials(true);
//        
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
//}