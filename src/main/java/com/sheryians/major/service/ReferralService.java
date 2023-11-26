package com.sheryians.major.service;

import com.sheryians.major.domain.Referral;
import com.sheryians.major.domain.User;
import com.sheryians.major.repository.ReferralRepository;
import com.sheryians.major.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class ReferralService {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReferralService referralService;

    @Autowired
    ReferralRepository referralRepository;

    public String generateReferralCode(String username){
        User referrer = userService.findByUsername(username);

        String uniqueReferralCode = generateUniqueReferralCode();

        referrer.setReferralCode(uniqueReferralCode);

        userRepository.save(referrer);

        return uniqueReferralCode;
    }

    // Method to track referrals for a user
    public Referral getReferrals(String username) {
        User referrer = userService.findByUsername(username);

        // Retrieve completed referrals for the referrer
        return  referralRepository.findByReferrerAndCompleted(referrer, true);
    }

    // Method to handle a successful referral
    public void handleSuccessfulReferral(String referrerUsername, String referredUsername) {
        User referrer = userService.findByUsername(referrerUsername);
        User referred = userService.findByUsername(referredUsername);

        // Check if a referral already exists
        if (!referralRepository.existsByReferrerAndReferred(referrer, referred)) {
            Referral referral = new Referral();
            referral.setReferrer(referrer);
            referral.setReferred(referred);
            referral.setCompleted(true);

            // Save the referral
            referralRepository.save(referral);

            // Provide rewards to the referrer and the referred (you may implement this logic)
            provideReferralRewards(referrer, referred);
        }
    }

    // Additional business logic for providing rewards to referrer and referred
    private void provideReferralRewards(User referrer, User referred) {
        // Implement the logic to provide rewards, e.g., sending discount codes
        // This can involve interacting with other parts of your application or external services
    }

    // Method to generate a unique referral code (you may implement this based on your requirements)
    private String generateUniqueReferralCode() {
        int length = 6;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder randomString = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            randomString.append(characters.charAt(randomIndex));
        }

        return randomString.toString();
    }


    public boolean userHasReferred(User user){
        if (referralRepository.findByReferred(user)!=null){
        return true;

        }else{
            return false;
        }
    }

    public boolean userHasReferrer(User user){
        if (referralRepository.findByReferrer(user)!=null){
            return true;

        }else{
            return false;
        }
    }
}
