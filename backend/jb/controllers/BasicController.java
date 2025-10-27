package com.jb.jb.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jb.jb.entities.*;
import com.jb.jb.repositories.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.nio.file.FileStore;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class BasicController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    InstaMemoryRepository instaMemoryRepository;

    @Autowired
    BlogRepository blogRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LimitedEditionRepository limitedEditionRepository;


    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    TestimonialRepository testimonialRepository;

    @GetMapping("")
    public String index(Model model) {
        // Insta Memories
        List<InstaMemory> memories = instaMemoryRepository.findAll();
        model.addAttribute("memories", memories);

        // Blogs
        List<BlogEntity> blogs = blogRepository.findAll();
        model.addAttribute("blogs", blogs);

        // Distinct categories
        List<String> categories = productRepository.findDistinctCategories();
        model.addAttribute("categories", categories);

        // Latest product for each category (for Deal of the Day tab)
        Map<String, ProductEntity> latestProductByCategory = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        for (String category : categories) {
            List<ProductEntity> products = productRepository.findTop1ByCategoryOrderByCreatedAtDesc(category);
            if (!products.isEmpty()) {
                ProductEntity product = products.get(0);
                try {
                    List<BigDecimal> prices = objectMapper.readValue(product.getPricesJson(), new TypeReference<>() {});
                    if (!prices.isEmpty()) {
                        product.setFirstPrice(prices.get(0));
                    }
                } catch (Exception e) {
                    product.setFirstPrice(BigDecimal.ZERO);
                }
                latestProductByCategory.put(category.toLowerCase(), product); // use lowercase keys for tab IDs
            }
        }
        model.addAttribute("latestProductByCategory", latestProductByCategory);

        // Featured product list (for carousel or general display)
        List<ProductEntity> allCategoryFirstProducts = new ArrayList<>(latestProductByCategory.values());

        // Partition featured list into chunks of 10
        List<List<ProductEntity>> partitionedProducts = new ArrayList<>();
        int chunkSize = 10;
        for (int i = 0; i < allCategoryFirstProducts.size(); i += chunkSize) {
            partitionedProducts.add(allCategoryFirstProducts.subList(i, Math.min(i + chunkSize, allCategoryFirstProducts.size())));
        }
        model.addAttribute("partitionedProducts", partitionedProducts);

        // Latest 6 products
        List<ProductEntity> products = productRepository.findTop6ByOrderByCreatedAtDesc();
        for (ProductEntity product : products) {
            try {
                List<BigDecimal> prices = objectMapper.readValue(product.getPricesJson(), new TypeReference<>() {});
                if (!prices.isEmpty()) {
                    product.setFirstPrice(prices.get(0));
                }
            } catch (Exception e) {
                product.setFirstPrice(BigDecimal.ZERO);
            }
        }
        model.addAttribute("products", products);

        // Limited Edition
        List<LimitedEditionEntity> limitedProducts = limitedEditionRepository.findAll();
        for (LimitedEditionEntity product : limitedProducts) {
            try {
                List<BigDecimal> prices = objectMapper.readValue(product.getPricesJson(), new TypeReference<>() {});
                if (!prices.isEmpty()) {
                    product.setFirstPrice(prices.get(0));
                }
            } catch (Exception e) {
                product.setFirstPrice(BigDecimal.ZERO);
            }
        }
        model.addAttribute("limitedEditionProducts", limitedProducts);


        //testimonial
        List<TestimonialEntity> testimonial=testimonialRepository.findAll();
        model.addAttribute("testimonials", testimonial);
        return "html/index";
    }






    @GetMapping("/shop")
    public String showShop(Model model, HttpServletRequest request) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("_csrf", csrfToken);


        List<ProductEntity> products = productRepository.findAll();

        // Extract first price from pricesJson for each product
        ObjectMapper objectMapper = new ObjectMapper();
        for (ProductEntity product : products) {
            try {
                List<BigDecimal> priceList = objectMapper.readValue(product.getPricesJson(), new TypeReference<List<BigDecimal>>() {});
                if (!priceList.isEmpty()) {
                    product.setPricesJson(priceList.get(0).toString()); // Overwrite with the first price as String
                }
            } catch (Exception e) {
                product.setPricesJson("0"); // fallback in case of JSON parse error
            }
        }

        model.addAttribute("products", products);
        model.addAttribute("categories", productRepository.findDistinctCategories());






        return "html/shop";
    }
    @GetMapping("/shop/category/{category}")
    public String showShopByCategory(@org.springframework.web.bind.annotation.PathVariable String category, Model model) {
        List<ProductEntity> products = productRepository.findByCategoryIgnoreCase(category);
        List<String> categories = productRepository.findDistinctCategories();

        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        return "html/shop";
    }


    @GetMapping("/register")
    public String register(){
        return "html/signup";
    }

    @GetMapping("/userLogin")
    public String userLogin(){
        return "html/login";
    }

    @PostMapping("/userLogin")
    public String loginUser(@RequestParam String email,
                            @RequestParam String password,
                            HttpSession session,
                            Model model) {

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty() || !userOptional.get().getPassword().equals(password)) {
            model.addAttribute("error", "Invalid email or password");
            return "html/login"; // show same page with error message
        }

        User user = userOptional.get();
        session.setAttribute("loggedInUser", user);
        return "redirect:/";
    }


    @GetMapping("/userLogout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/userLogin";
    }




    @Autowired
    private WishlistRepository wishlistRepository;



    @PostMapping("/wishlist/add/{productId}")
    public String addToWishlist(@PathVariable Long productId, Principal principal, RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/userLogin"; // Redirect if not logged in
        }

        ProductEntity product = productRepository.findById(productId).orElse(null);
        Optional<User> optionalUser = userRepository.findByEmail(principal.getName());

        if (product != null && optionalUser.isPresent()) {
            User user = optionalUser.get();

            boolean exists = wishlistRepository.existsByUserAndProduct(user, product);
            if (!exists) {
                Wishlist wishlist = new Wishlist();
                wishlist.setProduct(product);
                wishlist.setUser(user);
                wishlistRepository.save(wishlist);
                redirectAttributes.addFlashAttribute("success", "Added to Wishlist!");
            } else {
                redirectAttributes.addFlashAttribute("info", "Product already in Wishlist!");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Unable to add to wishlist.");
        }

        return "redirect:/shop";
    }




    private BigDecimal extractFirstPrice(String pricesJson) {
        try {
            List<BigDecimal> prices = objectMapper.readValue(pricesJson, new TypeReference<List<BigDecimal>>() {});
            return prices.isEmpty() ? null : prices.get(0);
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/shop/filter")
    public String filterByPrice(@RequestParam(required = false) BigDecimal min,
                                @RequestParam(required = false) BigDecimal max,
                                Model model) {
        List<ProductEntity> allProducts = productRepository.findAll();

        List<ProductEntity> filteredProducts = allProducts.stream()
                .filter(product -> {
                    BigDecimal firstPrice = extractFirstPrice(product.getPricesJson());
                    if (firstPrice == null) return false;
                    if (min != null && firstPrice.compareTo(min) < 0) return false;
                    if (max != null && firstPrice.compareTo(max) > 0) return false;
                    return true;
                })
                .collect(Collectors.toList());

        model.addAttribute("products", filteredProducts);
        return "html/shop";
    }

}
