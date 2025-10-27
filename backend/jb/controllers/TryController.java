package com.jb.jb.controllers;

import com.jb.jb.entities.TryEntity;
import com.jb.jb.repositories.TryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TryController {

    @Autowired
    TryRepository tryRepository;

    @GetMapping("/admin/tryAddProduct")
    public String addProduct(Model model){
        model.addAttribute("product",new TryEntity());
        return "/admin/tryAddProduct";
    }

    @PostMapping("/admin/tryAddProduct")
    public String saveProduct(@ModelAttribute("product") TryEntity tryEntity, RedirectAttributes redirectAttributes){
        if(tryRepository.existsBySku(tryEntity.getSku())){
            redirectAttributes.addFlashAttribute("error", "SKU '" + tryEntity.getSku() + "' already exists");
            return "redirect:/admin/tryAddProduct";
        }
        tryRepository.save(tryEntity);
        redirectAttributes.addFlashAttribute("success", "Product Added Successfully");
        return "redirect:/admin/tryAddProduct";
    }
}
