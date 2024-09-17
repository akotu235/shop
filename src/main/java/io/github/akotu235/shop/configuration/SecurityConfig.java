package io.github.akotu235.shop.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/**").csrf().disable();
        http.headers().frameOptions().sameOrigin();
        http.authorizeHttpRequests()
                .requestMatchers("/seller-panel/**").hasRole("SELLER")
                .and().authorizeHttpRequests()
                .requestMatchers("/user/**").authenticated()
                .and().authorizeHttpRequests()
                .requestMatchers("/cart/**").authenticated()
                .and().authorizeHttpRequests()
                .requestMatchers("/order/**").authenticated()
                .anyRequest().permitAll();
        http.formLogin()
//                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .permitAll();
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}