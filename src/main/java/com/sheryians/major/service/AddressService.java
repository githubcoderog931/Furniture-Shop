package com.sheryians.major.service;

import com.sheryians.major.domain.Address;
import com.sheryians.major.domain.User;
import com.sheryians.major.repository.AddressRepository;
import com.sheryians.major.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {


    @Autowired
    AddressRepository addressRepository;

    @Autowired
    UserRepository userRepository;

//    public String getStreetNamesByUser() {
//        return addressRepository.findStreetNamesByUser();
//    }
//    public String getStateNamesByUser() {
//        return addressRepository.findStateNamesByUser();
//    }
//    public String getCityNamesByUser() {
//        return addressRepository.findCityNamesByUser();
//    }

//    public String getUserName(String userId){
//        return addressRepository.findByUser_id(userId);
//    }
//


    // get all address of user

    public List<Address> getAllAddress(User user) {
        if (user != null) {
            return addressRepository.findByUser(user);
        }
        return null;
    }

    public List<Address> findAllUsersAddress(Integer Id){
        return addressRepository.findAllByUserId(Id);
    }


    public Address findById(Long id){
        return addressRepository.findById(id).orElse(null);
    }

    public void deleteAddress(Long id){
        addressRepository.deleteById(id);
    }

//    public Address setDefaultAddress(Long addressId) {
//        Address newDefaultAddress = addressRepository.findById(addressId).orElse(null);
//        if (newDefaultAddress != null) {
//
//            newDefaultAddress.setDefault(true);
//            addressRepository.save(newDefaultAddress);
//
//            addressRepository.findAll().stream()
//                    .filter(address -> !address.getId().equals(addressId))
//                    .forEach(address -> {
//                        address.setDefault(false);
//                        addressRepository.save(address);
//                    });
//        }
//        return newDefaultAddress;


}
