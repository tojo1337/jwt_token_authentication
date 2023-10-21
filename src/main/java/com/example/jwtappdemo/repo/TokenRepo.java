package com.example.jwtappdemo.repo;

import com.example.jwtappdemo.dto.jwt.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepo extends JpaRepository<JwtToken,Integer> {
    // Need to do it later
    // List<JwtToken> findAllValidTokenByUser(Integer id);
    Optional<JwtToken> findByToken(String jwtToken);
}
