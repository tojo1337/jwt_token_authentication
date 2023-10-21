package com.example.jwtappdemo.dto.responce;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TokenRequest {
    private String token;
}
