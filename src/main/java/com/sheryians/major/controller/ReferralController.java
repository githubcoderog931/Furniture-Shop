package com.sheryians.major.controller;

import com.sheryians.major.domain.Referral;
import com.sheryians.major.domain.User;
import com.sheryians.major.repository.ReferralRepository;
import com.sheryians.major.repository.UserRepository;
import com.sheryians.major.service.ReferralService;
import com.sheryians.major.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class ReferralController {
        @Autowired
        private ReferralService referralService;

        @Autowired
        private UserService userService;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private ReferralRepository referralRepository;

        @GetMapping("/generate")
        public String generateReferralCode(RedirectAttributes redirectAttributes, Principal principal) {
            Referral referral = new Referral();
            User user = userService.findByUsername(principal.getName());
            String referralCode = referralService.generateReferralCode(principal.getName());
            redirectAttributes.addFlashAttribute("referralCode", referralCode);
            referral.setReferralCode(referralCode);
            referral.setCompleted(false);
            referral.setReferrerPurchase(false);
            referral.setReferredPurchase(false);
            referral.setReferrer(user);
            referralRepository.save(referral);
            System.out.println();
            return "redirect:/user";
        }

        @GetMapping("/track")
        public String trackReferrals(Model model, Principal principal) {

            Referral referrals = referralService.getReferrals(principal.getName());
            model.addAttribute("referrals", referrals);
            System.out.println(referrals);
            return "trackReferral";
        }

        @PostMapping("/applyCode")
        public String applyReferralCode(@RequestParam("referral") String referralCode, Model model, Principal principal){
            System.out.println(referralCode);
            User referrerUser = userService.getUserByEmail(principal.getName());
            User user = userRepository.findByReferralCode(referralCode);
            if (user!=null) {
                Referral referral = referralRepository.findByReferralCode(referralCode);
                referral.setReferred(referrerUser);
                referral.setReferrerPurchase(true);
                referral.setReferredPurchase(true);
                referral.setCompleted(true);
                referralRepository.save(referral);
            }
                return "redirect:/user";

        }

//    @GetMapping("/admin/track-all")
//    public String trackAllReferrals(Model model) {
//        List<Referral> allReferrals = referralService.getAllReferrals();
//        model.addAttribute("allReferrals", allReferrals);
//        return "admin_track_referrals"; // Assuming you have a separate HTML file for admin tracking
//    }

}
