package org.fitness.user_service.config;

import com.password4j.Password;
import lombok.RequiredArgsConstructor;
import org.fitness.user_service.service.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    private final MyUserDetailsService myUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return Password.hash(rawPassword.toString())
                        .withArgon2()
                        .getResult();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return Password.check(rawPassword.toString(), encodedPassword)
                        .withArgon2();
            }
        };
    }

//    // ✅ Chain 1 — Actuator (no auth required, checked first)
//    @Bean
//    @Order(1)
//    public SecurityFilterChain actuatorFilterChain(HttpSecurity http) throws Exception {
//        http
//                .securityMatcher("/actuator/**")        // only matches actuator paths
//                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().permitAll()
//                )
//                .csrf(AbstractHttpConfigurer::disable);
//
//        return http.build();
//    }

    // ✅ Chain 2 — Everything else (JWT protected)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(
                        auth -> auth
                        .requestMatchers("/api/v1/users/**"
                        ,"/error").permitAll()
                        .anyRequest().authenticated()
                )
                .userDetailsService(myUserDetailsService);


        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
