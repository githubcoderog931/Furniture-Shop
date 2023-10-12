package com.sheryians.major.service;




import com.sheryians.major.domain.Product;
import com.sheryians.major.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {



    public List<User> getAllUsers();

    public Optional<User> findById(int id);

    public void save(User user);

    public void delete(User user);

    Product getCategoryOfProduct(String name);

}
