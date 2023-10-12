package com.sheryians.major.service;



import org.springframework.security.core.userdetails.UserDetails;


import java.util.Optional;

public interface CustomUserDetailService {
    public UserDetails loadUserByUsername(String email);
}
