package com.example.jwtappdemo.repo;

import com.example.jwtappdemo.dto.user.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyUserRepo extends JpaRepository<MyUser,Integer> {
    // No need to implement any other method as it would have already been extended
}
