package com.sheryians.major.service;



import com.sheryians.major.domain.Address;
import com.sheryians.major.domain.Product;
import com.sheryians.major.domain.User;
import com.sheryians.major.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Optional<User> findById(int id){return  userRepository.findById(id);}

    public void save(User user){userRepository.save(user);}

    public void delete(User user){userRepository.delete(user);}

    public Product getCategoryOfProduct(String name) {
        return null;
    }

    public User findByUsername(String username) {
        return userRepository.findByEmail(username);
    }

    public String firstnameByEmail(String email){
        User name = userRepository.findByEmail(email);
        return name.getFirstname();
    }

    public String lastnameByEmail(String email){
        User name = userRepository.findByEmail(email);
        return name.getLastname();
    }

    public User getUserByEmail(String name) {
       return userRepository.findByEmail(name);
    }

    public void removeUserById(Integer id) {
        userRepository.deleteById(id);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }


//    public boolean doesUserHaveAddress(String username){
//        User user = userRepository.findByEmail(username);
//        return user != null && user.getAddresses() != null && !user.getAddresses().isEmpty();
//    }
//
//    public List<Address> getUserAddress(String username){
//        User user = userRepository.findByEmail(username);
//        Integer UserId = user.getId();
//        if (user.getId() != null &&  != null){
//            return user.getAddresses();
//        }
//        return Collections.emptyList();
//    }



}
