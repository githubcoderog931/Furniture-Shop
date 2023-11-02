//package com.sheryians.major.service;
//
//import com.sheryians.major.domain.Otp;
//import com.sheryians.major.domain.User;
//import com.sheryians.major.repository.OtpRepository;
//import com.sheryians.major.repository.UserRepository;
//import org.apache.commons.lang3.RandomStringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.util.Date;
//
//@Service
//public class OtpService {
//
//    @Autowired
//    private OtpRepository otpRepository;
//    @Autowired
//    private UserRepository userRepository;
//
//
//
//
//    public Otp findOtpByUserId(String userId) {
//        return otpRepository.findById(userId);
//    }
//
//    public boolean isOtpValid(String email, String enteredOTP) {
//        Otp otp = otpRepository.findByUser(email);
//        System.out.println(otp.getOtpValue());
//
//        if (otp != null) {
//            String storedOTP = otp.getOtpValue();
//            if (storedOTP != null && storedOTP.equals(enteredOTP) && isOtpNotExpired(otp)) {
//                return true; // Valid OTP
//            }
//        }
//
//        return false; // Invalid OTP or expired
//    }
//
//
//
//
//    private boolean isOtpNotExpired(Otp otp) {
//        // Get the current time
//        Date currentTime = new Date();
//
//        // Check if the current time is before the expiration time
//        return currentTime.before(otp.getExpirationTime());
//    }
//
//    public String generateOTP(String email) {
//        return RandomStringUtils.randomNumeric(6);
//    }
//
//    // Additional methods for generating and validating OTPs, if needed
//
//    public String generateAndSaveOTP(String email) {
//        String generatedOTP = generateOTP(email); // You already have a method to generate OTP
//
//
//        Otp otp = new Otp();
//        otp.setUser(userRepository.findByEmail(email));
//        otp.setOtpValue(generatedOTP);
//
//// Set the expiration time to 5 minutes from the current time
//        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(5);
//
//// Convert LocalDateTime to Date
//        Date expirationDate = Date.from(expirationTime.atZone(ZoneId.systemDefault()).toInstant());
//
//        otp.setExpirationTime(expirationDate);
//
//        otpRepository.save(otp); // Save the OTP to the database
//        return generatedOTP; // Return the generated OTP for email sending
//    }
//
//
//
//}
//
