package com.example.jwtappdemo.filter;

import com.example.jwtappdemo.repo.TokenRepo;
import com.example.jwtappdemo.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.logging.Logger;

public class JwtTokenFilter extends OncePerRequestFilter {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private TokenRepo tokenRepo;
    private Logger log = Logger.getLogger(this.getClass().getName());
    @Override
    @SneakyThrows
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        String path = request.getServletPath();
        if(path.contains("/api/v1/sign-in")||path.contains("/api/v1/token")||path.contains("/api/v1/open")){
            filterChain.doFilter(request,response);
            return;
        }else {
            log.info("Once per request authentication filter reached");
            String authHeader = request.getHeader("Authorization");
            if(authHeader==null||!authHeader.startsWith("Bearer ")){
                filterChain.doFilter(request,response);
                return;
            }
            String jwtToken = authHeader.substring(7);
            String user = jwtService.extractUsername(jwtToken);
            if(user!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                UserDetails userDetails = userDetailsService.loadUserByUsername(user);
                boolean isValidToken = tokenRepo.findByToken(jwtToken).map(token->!token.isExpired() && !token.isRevoked()).orElse(false);
                if(jwtService.isValidToken(jwtToken,userDetails) && isValidToken){
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request,response);
            return;
        }
    }
}
