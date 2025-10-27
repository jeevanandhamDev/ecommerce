//package com.jb.jb.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable() // Optional: disable CSRF if needed
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/", "/register", "/userLogin").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .formLogin(form -> form
//                        .loginPage("/userLogin")
//                        .defaultSuccessUrl("/", true)
//                        .permitAll()
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/userLogout")
//                        .logoutSuccessUrl("/userLogin")
//                        .permitAll()
//                );
//
//        return http.build();
//    }
//}
//
//
//
