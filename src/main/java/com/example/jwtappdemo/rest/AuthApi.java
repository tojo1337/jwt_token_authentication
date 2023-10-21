package com.example.jwtappdemo.rest;

import com.example.jwtappdemo.dto.jwt.JwtToken;
import com.example.jwtappdemo.dto.responce.AuthRequest;
import com.example.jwtappdemo.dto.responce.AuthResponce;
import com.example.jwtappdemo.dto.responce.TokenRequest;
import com.example.jwtappdemo.service.AuthService;
import com.example.jwtappdemo.service.JwtService;
import com.example.jwtappdemo.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/")
public class AuthApi {
    /*
     *  Do the portion of registration and token granting properly
     *  It is essential to our application
     *  Then we need to add the jwt token repo or whatever we are intending to do
     */
    @Autowired
    private MyUserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthService authService;
    private Logger log = Logger.getLogger(this.getClass().getName());
    @PostMapping("/sign-in")
    public AuthResponce signIn(@RequestBody AuthRequest authRequest){
        log.info("signIn method invoked");
        String resp = userDetailsService.save(authRequest);
        return AuthResponce.builder().data(resp).build();
    }
    @PostMapping("/token")
    public AuthResponce genToken(@RequestBody AuthRequest authRequest){
        log.info("getToken method invoked");
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUser(),authRequest.getPass()));
        if(auth.isAuthenticated()){
            log.info("Authentication credentials matched");
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            log.info("Logged in username : "+userDetails.getUsername());
            String tokenStr = jwtService.generateToken(userDetails);
            authService.revokeAllUserToken(userDetails);
            authService.saveUserToken(userDetails,tokenStr);
            return AuthResponce.builder().data(tokenStr).build();
        }
        return AuthResponce.builder().data("Unable to create token").build();
    }
    @PostMapping("/refresh")
    public AuthResponce refreshToken(@RequestBody TokenRequest tokenRequest){
        log.log(Level.WARNING,"This method has not been implemented yet");
        return AuthResponce.builder().data(tokenRequest.getExpiredToken()).build();
    }
}
