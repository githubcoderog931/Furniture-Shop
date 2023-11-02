package com.sheryians.major.service;

import com.sheryians.major.domain.User;
import com.sheryians.major.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    //define Autowired

    @Autowired
    UserRepository userRepository;




    public void editProfile(String username,  String firstname, String lastname){
        User user = userRepository.findByEmail(username);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        userRepository.save(user);
    }
}
