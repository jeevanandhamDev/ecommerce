package com.jb.jb.controllers;

import com.jb.jb.entities.ProductEntity;
import com.jb.jb.entities.User;
import com.jb.jb.entities.Wishlist;
import com.jb.jb.repositories.ProductRepository;
import com.jb.jb.repositories.UserRepository;
import com.jb.jb.repositories.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/wishlist")
public class WishlistController {

//    @Autowired
//    private WishlistRepository wishlistRepository;
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @PostMapping("/add/{productId}")
//    public String addToWishlist(@PathVariable Long productId, Principal principal, RedirectAttributes redirectAttributes) {
//        if (principal == null) {
//            return "redirect:/login"; // User not logged in
//        }
//
//        ProductEntity product = productRepository.findById(productId).orElse(null);
//        Optional<User> optionalUser = userRepository.findByEmail(principal.getName());
//
//        if (product != null && optionalUser.isPresent()) {
//            User user = optionalUser.get();
//
//            Wishlist wishlist = new Wishlist();
//            wishlist.setProduct(product);
//            wishlist.setUser(user);
//            wishlistRepository.save(wishlist);
//
//            redirectAttributes.addFlashAttribute("success", "Added to Wishlist!");
//        } else {
//            redirectAttributes.addFlashAttribute("error", "Unable to add to wishlist.");
//        }
//
//        return "redirect:/products/" + productId;
//    }
}

