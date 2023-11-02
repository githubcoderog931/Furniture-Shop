package com.sheryians.major.repository;


import com.sheryians.major.domain.Address;
import com.sheryians.major.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {

    // find
    List<Address> findByUser(User user);

    List<Address> findAllByUserId(Integer Id);

    boolean existsByUserId(Integer userId);

}
