package com.jb.jb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ContactController {

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/contactUs")
    public String contactUsForm() {
        return "html/contact";  // your contact page
    }

    @PostMapping("/contactUs")
    public String handleContactForm(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String message,
            RedirectAttributes redirectAttributes
    ) {
        try {
            // Prepare the email
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo("jeevarlpjrlpj@gmail.com");
            mailMessage.setSubject("New Contact Form Submission");
            mailMessage.setText("Name: " + name + "\nEmail: " + email + "\nMessage:\n" + message);

            // Send the email
            mailSender.send(mailMessage);

            // Add success message to be shown on the page
            redirectAttributes.addFlashAttribute("successMessage", "Email sent successfully!");

        } catch (Exception e) {
            // Add error message
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to send email. Please try again.");
        }

        // Redirect back to contact form
        return "redirect:/contactUs";
    }

}
