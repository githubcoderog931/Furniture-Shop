package com.sheryians.major.service;


import com.sheryians.major.domain.User;
import com.sheryians.major.domain.Wallet;
import com.sheryians.major.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    @Autowired
    WalletRepository walletRepository;

    // create wallet

    public Wallet createWallet(User user){
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setWalletAmount(0.0);
        walletRepository.save(wallet);
        return wallet;
    }


    // save wallet

    public void saveWallet(Wallet wallet){
        walletRepository.save(wallet);
    }

    // get wallet of user if exists

    public Wallet getWallet(User user){
        if (walletExists(user)){
            walletRepository.findByUser(user);
        }
        return createWallet(user);
    }

    public boolean walletExists(User user){
        if(user.getWallet()!=null){
            return true;
        }
        return false;
    }

}
