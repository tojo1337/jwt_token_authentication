package com.example.jwtappdemo.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
public class SampleApi {
    @GetMapping("/secured")
    public String secured(){
        // Need to add the user token mapping to make the authentication work properly
        return "Hello to the secured endpoint";
    }
    @GetMapping("/open")
    public String openApi(){
        return "Hello to the open api";
    }
}
