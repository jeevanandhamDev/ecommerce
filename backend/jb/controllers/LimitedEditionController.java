package com.jb.jb.controllers;


import com.jb.jb.entities.InstaMemory;
import com.jb.jb.entities.LimitedEditionEntity;
import com.jb.jb.entities.ProductEntity;
import com.jb.jb.repositories.InstaMemoryRepository;
import com.jb.jb.repositories.LimitedEditionRepository;
import com.jb.jb.repositories.ProductRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("admin/")
public class LimitedEditionController {

    @Autowired
    LimitedEditionRepository limitedEditionRepository;

    @GetMapping("/limited-edition")
    public String showLimitedEditionProducts(Model model) {
        List<LimitedEditionEntity> products = limitedEditionRepository.findAll();
        model.addAttribute("limitedEditionProducts", products);
        return "admin/leForm"; // Thymeleaf view name (limited-edition.html)
    }

    @PostMapping("/limited-edition")
    public String saveProduct(
            @RequestParam("product_name") String productName,
            @RequestParam("product_description") String description,
            @RequestParam("sku") String sku,
            @RequestParam("images") MultipartFile image,
            @RequestParam("category") String category,
            @RequestParam("weights[]") List<String> weights,
            @RequestParam("prices[]") List<String> prices,
            RedirectAttributes redirectAttributes
    ) throws IOException {

        if (limitedEditionRepository.existsBySku(sku)) {
            redirectAttributes.addFlashAttribute("errorMessage", "SKU '" + sku + "' already exists!");
            return "redirect:/admin/leAdd";
        }

        LimitedEditionEntity product = new LimitedEditionEntity();
        product.setProductName(productName);
        product.setProductDescription(description);
        product.setSku(sku);
        product.setCategory(category);

        // Save image
        String imagePath = saveImage(image);
        product.setImagePath(imagePath);

        // Store weights and prices as JSON-style strings
        product.setWeightsJson(weights.toString());
        product.setPricesJson(prices.toString());

        limitedEditionRepository.save(product);
        redirectAttributes.addFlashAttribute("successMessage", "Product added successfully!");
        return "redirect:/admin/limited-edition";
    }

    // Helper method to save uploaded image
    private String saveImage(MultipartFile image) {
        try {
            String imageName = UUID.randomUUID().toString() + "_" + StringUtils.cleanPath(image.getOriginalFilename());
            String uploadDir = System.getProperty("user.dir") + "/uploads/";
            File uploadPath = new File(uploadDir);
            if (!uploadPath.exists()) uploadPath.mkdirs();

            Path path = Paths.get(uploadDir + imageName);
            Files.copy(image.getInputStream(), path);
            return "/uploads/" + imageName;  // Use relative path for web
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
