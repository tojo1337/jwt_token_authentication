package com.example.jwtappdemo.service;

import com.example.jwtappdemo.dto.responce.AuthRequest;
import com.example.jwtappdemo.dto.user.MyUser;
import com.example.jwtappdemo.dto.user.MyUserDetails;
import com.example.jwtappdemo.repo.MyUserRepo;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private MyUserRepo repo;
    @Override
    @SneakyThrows
    public UserDetails loadUserByUsername(String username){
        List<MyUser> list = repo.findAll();
        MyUserDetails details = null;
        for(MyUser user:list){
            if(user.getName().equals(username)){
                details = new MyUserDetails(user);
                return (UserDetails) details;
            }
        }
        throw new UsernameNotFoundException("No user exists with the username : "+username);
    }
    public String save(AuthRequest authRequest){
        List<MyUser> list = repo.findAll();
        MyUser entry = null;
        for(MyUser data:list){
            if(data.getName().equals(authRequest.getUser())){
                entry = data;
                break;
            }
        }
        if(entry!=null){
            return "Username already exists";
        }else {
            MyUser user = new MyUser();
            user.setName(authRequest.getUser());
            user.setPassword(getPasswordEncoder().encode(authRequest.getPass()));
            repo.save(user);
            return "User saved";
        }
    }
    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
