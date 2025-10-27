package com.jb.jb.controllers;

import com.jb.jb.entities.User;
import com.jb.jb.repositories.UserRepository;
import com.jb.jb.services.OtpService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@Controller
public class AccountController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/account")
    public String myAccount(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/userLogin";
        model.addAttribute("user", loggedInUser);
        return "html/account";
    }

    @PostMapping("/account/update")
    public String updateAccount(@ModelAttribute User updatedUser, HttpSession session) {
        User currentUser = (User) session.getAttribute("loggedInUser");
        if (currentUser == null) return "redirect:/userLogin";

        // Only update editable fields
        currentUser.setName(updatedUser.getName());
        currentUser.setPhoneNumber(updatedUser.getPhoneNumber());
        currentUser.setAddress(updatedUser.getAddress());

        userRepository.save(currentUser);
        session.setAttribute("loggedInUser", currentUser);

        return "redirect:/account";
    }

    @GetMapping("/account/change-email")
    public String changeEmailForm() {
        return "html/change-email"; // Create a form for email + OTPs
    }

    // Add more for OTP logic (see below)

    @Autowired
    private OtpService otpService;

    @PostMapping("/account/request-current-email-otp")
    @ResponseBody
    public String requestCurrentEmailOtp(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "Not logged in";
        otpService.generateAndSendOtp(user.getEmail());
        return "OTP sent to your current email!";
    }


    @PostMapping("/account/request-email-otp")
    @ResponseBody
    public String requestEmailOtp(@RequestParam String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            return "Email already in use!";
        }
        otpService.generateAndSendOtp(email);
        return "OTP sent!";
    }


    @PostMapping("/account/verify-email-change")
    public String verifyAndUpdateEmail(@RequestParam String currentOtp,
                                       @RequestParam String newEmail,
                                       @RequestParam String newOtp,
                                       HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/userLogin";

        boolean oldVerified = otpService.verifyOtp(user.getEmail(), currentOtp);
        boolean newVerified = otpService.verifyOtp(newEmail, newOtp);

        if (oldVerified && newVerified) {
            user.setEmail(newEmail);
            userRepository.save(user);
            session.setAttribute("loggedInUser", user);
            return "redirect:/account";
        }

        return "redirect:/account/change-email?error=Invalid OTP";
    }

}
