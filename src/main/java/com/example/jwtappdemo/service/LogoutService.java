package com.example.jwtappdemo.service;

import com.example.jwtappdemo.dto.jwt.JwtToken;
import com.example.jwtappdemo.repo.TokenRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
public class LogoutService implements LogoutHandler {
    @Autowired
    private TokenRepo tokenRepo;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authHeader = request.getHeader("Authorization");
        if(authHeader==null||!authHeader.startsWith("Bearer ")){
            return;
        }else {
            String jwtToken = authHeader.substring(7);
            JwtToken token = tokenRepo.findByToken(jwtToken).orElse(null);
            if(token!=null){
                token.setExpired(true);
                token.setRevoked(true);
                tokenRepo.save(token);
                SecurityContextHolder.clearContext();
            }
        }
    }
}
