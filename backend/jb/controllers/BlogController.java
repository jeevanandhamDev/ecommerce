package com.jb.jb.controllers;

import com.jb.jb.entities.BlogEntity;
import com.jb.jb.repositories.BlogRepository;
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
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("admin/")
public class BlogController {

    @Autowired
    BlogRepository blogRepository;

    @GetMapping("blogAdd")
    public String blogAddForm(Model model){
        model.addAttribute("blog", new BlogEntity());
        return "/admin/blogAdd";
    }


    // Directory to save uploaded images (you can change this)
    private static final String UPLOAD_DIR = "C:/spring/jb/uploads/blog/";
    @PostMapping("blogAdd")
    public String blogAdd(
            @ModelAttribute("blog") BlogEntity blogEntity,
            @RequestParam("imageFile") MultipartFile imageFile,
            RedirectAttributes redirectAttributes) {

        try {
            // Handle image upload
            if (!imageFile.isEmpty()) {
                // Create upload dir if it doesn't exist
                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                // Generate a unique filename
                String originalFilename = StringUtils.cleanPath(imageFile.getOriginalFilename());
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

                // Save the file
                Path filePath = Paths.get(UPLOAD_DIR + uniqueFileName);
                Files.write(filePath, imageFile.getBytes());

                // Set imagePath to save relative path
                blogEntity.setImagePath("/uploads/blog/" + uniqueFileName); // ✅ Correct

            }

            // Optional: Set date to today if not provided
            if (blogEntity.getDate() == null) {
                blogEntity.setDate(LocalDate.now());
            }

            // Save to database
            blogRepository.save(blogEntity);
            redirectAttributes.addFlashAttribute("success", "Blog added successfully!");

        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to upload image.");
        }

        return "redirect:/admin/blogAdd";
    }


    //manage blog
    @GetMapping("blogManage")
    public String blogManage(Model model){
        List<BlogEntity> blogEntity=blogRepository.findAll();
        model.addAttribute("blog", blogEntity);
        return "/admin/blogManage";
    }

    @GetMapping("blogDelete")
    public String blogDelete(@RequestParam("id") Long id, RedirectAttributes redirectAttributes){
        if(blogRepository.existsById(id)){
            blogRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Blog deleted successfully!");
        }
        else{
            redirectAttributes.addFlashAttribute("error", "something went wrong");
        }

        return "redirect:/admin/blogManage";
    }

}
