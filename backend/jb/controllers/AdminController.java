package com.jb.jb.controllers;

import com.jb.jb.entities.InstaMemory;
import com.jb.jb.entities.ProductEntity;
import com.jb.jb.entities.TestimonialEntity;
import com.jb.jb.repositories.InstaMemoryRepository;
import com.jb.jb.repositories.ProductRepository;
import com.jb.jb.repositories.TestimonialRepository;
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
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("admin/")
public class AdminController {

    private final ProductRepository productRepository;
    private final InstaMemoryRepository instaMemoryRepository;

    public AdminController(ProductRepository productRepository,InstaMemoryRepository instaMemoryRepository) {
        this.productRepository = productRepository;
        this.instaMemoryRepository=instaMemoryRepository;
    }


    @Autowired
    TestimonialRepository testimonialRepository;
    //testimonial
    @GetMapping("/testimonial-add")
    public String showTestimonialForm(Model model) {
        model.addAttribute("testimonial", new TestimonialEntity()); // Make sure this matches th:object
        return "admin/testimonial-add";
    }

    @PostMapping("/testimonial-add")
    public String insertTestimonial(
            @ModelAttribute("testimonial") TestimonialEntity testimonial,
            @RequestParam("imgFile") MultipartFile imgFile, // name matches HTML input
            Model model) throws IOException {

        String imagePath = saveImage(imgFile);
        testimonial.setImgPath(imagePath);
        testimonialRepository.save(testimonial);

        model.addAttribute("successMessage", "Testimonial saved successfully!");
        return "/admin/testimonial-add";
    }

    // Show All Testimonials
    @GetMapping("/testimonial-manage")
    public String manageTestimonials(Model model) {
        List<TestimonialEntity> testimonials = testimonialRepository.findAll();
        model.addAttribute("testimonials", testimonials);
        return "/admin/testimonial-manage";
    }

    // Delete Testimonial
    @GetMapping("/testimonialDelete")
    public String deleteTestimonial(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            testimonialRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Testimonial deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Unable to delete testimonial.");
        }
        return "redirect:/admin/testimonial-manage";
    }

    



    // Admin dashboard
    @GetMapping()
    public String adminDashboard() {
        return "/admin/dashboard";
    }

    // Show add product form
    @GetMapping("/ecommerce-products")
    public String addProduct(Model model) {
        model.addAttribute("product", new ProductEntity()); // empty form
        return "/admin/ecommerce-products";
    }

    // Show product management page
    @GetMapping("/manageProduct")
    public String manageProduct(Model model) {
        List<ProductEntity> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "/admin/manage_product";
    }


    // Save new product
    @PostMapping("/ecommerce-products")
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

        if (productRepository.existsBySku(sku)) {
            redirectAttributes.addFlashAttribute("errorMessage", "SKU '" + sku + "' already exists!");
            return "redirect:/admin/ecommerce-products";
        }

        ProductEntity product = new ProductEntity();
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

        productRepository.save(product);
        redirectAttributes.addFlashAttribute("successMessage", "Product added successfully!");
        return "redirect:/admin/ecommerce-products";
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

    // Delete product
    @GetMapping("/deleteProduct")
    public String deleteProduct(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Product not found!");
        }
        return "redirect:/admin/manageProduct";
    }

    // Show edit form
    @GetMapping("/editProduct/")
    public String showEditForm(@RequestParam("id") Long id, Model model, RedirectAttributes redirectAttributes){

    Optional<ProductEntity> product = productRepository.findById(id);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            return "/admin/edit-product";  // Your edit form view
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Product not found.");
            return "redirect:/admin/manageProduct";
        }
    }

    @PostMapping("/editProduct/{id}")
    public String updateProduct(@PathVariable("id") Long id,
                                @RequestParam("product_name") String productName,
                                @RequestParam("product_description") String description,
                                @RequestParam("sku") String sku,
                                @RequestParam("category") String category,
                                @RequestParam(value = "image", required = false) MultipartFile image,
                                @RequestParam("weights[]") List<String> weights,
                                @RequestParam("prices[]") List<String> prices,
                                RedirectAttributes redirectAttributes) {
        Optional<ProductEntity> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            ProductEntity product = existingProduct.get();
            product.setProductName(productName);
            product.setProductDescription(description);
            product.setSku(sku);
            product.setCategory(category);
            product.setWeightsJson(weights.toString());
            product.setPricesJson(prices.toString());

            if (image != null && !image.isEmpty()) {
                String imagePath = saveImage(image);
                product.setImagePath(imagePath);
            }

            productRepository.save(product);
            redirectAttributes.addFlashAttribute("successMessage", "Product updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Product not found.");
        }

        return "redirect:/admin/manageProduct";
    }





    //insta memory
    @GetMapping("/instaMemoryAdd")
    public String instaMemoryAdd(Model model){
        model.addAttribute("memory", new InstaMemory());
        return "/admin/instaMemoryAdd";
    }

    private final String uploadDir = "C:/spring/jb/uploads/";
    @PostMapping("/instaMemoryAdd")
    public String addMemory(@RequestParam("memory_description") String description,
                            @RequestParam("image") MultipartFile file,
                            Model model) {
        try {
            // 1. Save the file to the folder
            String fileName = file.getOriginalFilename();
            if (fileName != null && !fileName.isEmpty()) {
                File saveFile = new File(uploadDir + fileName);
                file.transferTo(saveFile);
            }

            // 2. Save data to DB
            InstaMemory memory = new InstaMemory();
            memory.setDescription(description);
            memory.setImagePath("/uploads/" + file.getOriginalFilename()); // Save relative path
            instaMemoryRepository.save(memory);

            model.addAttribute("successMessage", "Memory added successfully!");

        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Failed to upload image.");
        }

        return "redirect:/admin/instaMemoryManage"; //
    }

    @GetMapping("/instaMemoryManage")
    public String manageMemories(Model model) {
        model.addAttribute("memories", instaMemoryRepository.findAll());
        return "admin/instaMemoryManage";
    }


    @GetMapping("/instaMemoryDelete")
    public String deleteMemory(@RequestParam("id") Long id,
                               RedirectAttributes redirectAttributes) {
        if (instaMemoryRepository.existsById(id)) {
            instaMemoryRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Memory (ID " + id + ") deleted successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Memory with ID " + id + " not found.");
        }
        return "redirect:/admin/instaMemoryManage";
    }
}
