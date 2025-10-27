package com.jb.jb.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jb.jb.entities.CartItem;
import com.jb.jb.entities.ProductEntity;
import com.jb.jb.entities.User;
import com.jb.jb.repositories.CartItemRepository;
import com.jb.jb.repositories.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class CartController {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private HttpSession session;

    @Autowired
    private ProductRepository productRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/add-to-cart")
    @ResponseBody
    public String addToCart(@RequestParam Long productId) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "not_logged_in";

        ProductEntity product = productRepository.findById(productId).orElse(null);
        if (product == null) return "product_not_found";

        BigDecimal firstPrice = BigDecimal.ZERO;
        try {
            List<BigDecimal> prices = objectMapper.readValue(product.getPricesJson(), new TypeReference<List<BigDecimal>>() {});
            if (!prices.isEmpty()) {
                firstPrice = prices.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "price_parse_error";
        }

        CartItem item = new CartItem();
        item.setUser(user);
        item.setProductName(product.getProductName());
        item.setImageUrl(product.getImagePath());
        item.setPrice(firstPrice);
        item.setQuantity(1);

        cartItemRepository.save(item);
        return "success";
    }

    @GetMapping("/cart")
    public String viewCart(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if(user==null) return "redirect:/userLogin";
        List<CartItem> cartItems = cartItemRepository.findByUser(user);

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            totalAmount = totalAmount.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalAmount", totalAmount);

        return "html/cart";
    }


    @GetMapping("/cartRemove")
    public String removeCartItem(@RequestParam("id") Long itemId) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/userLogin";

        CartItem item = cartItemRepository.findById(itemId).orElse(null);

        if (item != null && item.getUser().getId().equals(user.getId())) {
            cartItemRepository.deleteById(itemId);
        }

        return "redirect:/cart";
    }

    @PostMapping("/update-cart-quantity")
    @ResponseBody
    public void updateCartQuantity(@RequestParam("id") Long itemId, @RequestParam("delta") int delta) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return;

        CartItem item = cartItemRepository.findById(itemId).orElse(null);
        if (item != null && item.getUser().getId().equals(user.getId())) {
            int updatedQty = item.getQuantity() + delta;
            if (updatedQty < 1) {
                cartItemRepository.deleteById(itemId); // Remove if quantity goes below 1
            } else {
                item.setQuantity(updatedQty);
                cartItemRepository.save(item);
            }
        }
    }

}
