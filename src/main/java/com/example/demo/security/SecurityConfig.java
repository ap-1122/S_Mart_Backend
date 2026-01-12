package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // Import Zaroori hai
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
            		
            		
  //ISS PURE PART KO SIRF TESTING KE LIYE COMMENT KIYAHAI AUR SARE POINT KO ALLOW IN LOWER CODE           		
//                // 1. Pre-flight requests (OPTIONS) ko hamesha allow karo (Fixes 403 on Upload)
//                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                
//                // 2. Public endpoints
//                .requestMatchers("/auth/**").permitAll()
//                .requestMatchers("/public/**").permitAll()
//
//                // 3. TESTING MODE: Admin & APIs Open
//                .requestMatchers(
//                    "/admin/**",
//                    "/api/brands/**",
//                    "/api/categories/**",
//                    "/api/products/**",     // Products Get/Post
//                    "/api/variants/**",
//                    "/api/attributes/**",
//                    "/api/product-images/**" // Image Upload
//                ).permitAll()
//
//                // 4. Baki sab band
//                .anyRequest().authenticated()
            		
            		
            		
           //YE SIRF TESTING KE LIYE SARE POIN ALOOW KIYA HAI 
            		// ⚠️ DEBUG MODE: Sab kuch allow kar do (Taki 403 ka kissa khatam ho)
                    .requestMatchers("/**").permitAll()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173")); // React
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*")); // Sab headers allow karo
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