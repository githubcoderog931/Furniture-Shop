package com.sheryians.major.service.impl;


import com.sheryians.major.domain.CustomUserDetail;
import com.sheryians.major.domain.User;
import com.sheryians.major.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired

    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        Optional<User> user = userRepository.findUserByEmail(email);
        user.orElseThrow(() -> new UsernameNotFoundException("User not found"));
//        if(!user.get().getEnable()){
//            throw new UsernameNotFoundException("User not enabled");
//        }
        return user.map(CustomUserDetail::new).get();
    }





}
