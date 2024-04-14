package com.vmdev.eshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static com.vmdev.eshop.entity.enums.Role.ADMIN;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(requests -> requests
                .requestMatchers(
                        "/login",
                        "/registration",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/orders/{id}/add")
                .permitAll()
                .requestMatchers(HttpMethod.POST, "/clients")
                .permitAll()
                .requestMatchers(
                        "/clients/**",
                        "/products/new",
                        "/products/{id}/*")
                .hasAuthority(ADMIN.getAuthority())
                .anyRequest().authenticated());
        http.csrf(AbstractHttpConfigurer::disable);
        http.formLogin(login -> login
                .loginPage("/login")
                .defaultSuccessUrl("/products")
        );

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
