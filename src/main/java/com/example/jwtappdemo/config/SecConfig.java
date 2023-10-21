package com.example.jwtappdemo.config;

import com.example.jwtappdemo.filter.JwtTokenFilter;
import com.example.jwtappdemo.service.MyUserDetailsService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecConfig {
    @Autowired
    private MyUserDetailsService userDetailsService;
    private final String[] overlook = {
            "/api/v1/sign-in",
            "/api/v1/refresh",
            "/api/v1/token",
            "/api/v1/open"
    };
    @Bean
    @SneakyThrows
    public SecurityFilterChain filterChain(HttpSecurity http){
        http.csrf(csrf->{
            csrf.disable();
        }).cors(cors->{
            cors.disable();
        }).authorizeHttpRequests(req->{
            req.requestMatchers(overlook).permitAll();
            req.anyRequest().authenticated();
        }).sessionManagement(session->{
            session.sessionCreationPolicy(STATELESS);
        }).authenticationProvider(
                authenticationProvider()
        ).addFilterBefore(
                jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class
        );
        return http.build();
    }
    @Bean
    public JwtTokenFilter jwtTokenFilter(){
        return new JwtTokenFilter();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    @Bean
    @SneakyThrows
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig){
        return authConfig.getAuthenticationManager();
    }
}
