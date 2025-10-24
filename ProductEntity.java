package com.jb.jb.entities;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class ProductEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name")
    @NotBlank(message = "Product name is required")
    private String productName;

    @Column(name = "description")
    private String productDescription;

    @NotBlank(message = "SKU is required")
   
    @Column(unique = true)
    private String sku;

    private String imagePath; // store image filename/path here

    private String category;

    // You can use a JSON string to store weights and prices if you don't want a separate table.
    @Lob
    private String weightsJson;

    @Lob
    private String pricesJson;


    @Transient
    private BigDecimal firstPrice;


    @Column(name = "price")
    private BigDecimal price;  // Add this field

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }


    private LocalDateTime createdAt;
    // Automatically set createdAt before persisting
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }



    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public BigDecimal getFirstPrice() {
        return firstPrice;
    }

    public void setFirstPrice(BigDecimal firstPrice) {
        this.firstPrice = firstPrice;
    }







    //getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getWeightsJson() {
        return weightsJson;
    }

    public void setWeightsJson(String weightsJson) {
        this.weightsJson = weightsJson;
    }

    public String getPricesJson() {
        return pricesJson;
    }

    public void setPricesJson(String pricesJson) {
        this.pricesJson = pricesJson;
    }
}
