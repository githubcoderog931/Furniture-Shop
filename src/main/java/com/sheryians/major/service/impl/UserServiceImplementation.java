package com.sheryians.major.service.impl;



import com.sheryians.major.domain.Product;
import com.sheryians.major.domain.User;
import com.sheryians.major.repository.UserRepository;
import com.sheryians.major.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    UserRepository userRepository;

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Optional<User> findById(int id){return  userRepository.findById(id);}

    public void save(User user){userRepository.save(user);}

    public void delete(User user){userRepository.delete(user);}

    @Override
    public Product getCategoryOfProduct(String name) {
        return null;
    }

}
