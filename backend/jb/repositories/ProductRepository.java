package com.jb.jb.repositories;

import com.jb.jb.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    boolean existsBySku(String sku);

    @Query("SELECT DISTINCT p.category FROM ProductEntity p")
    List<String> findDistinctCategories();

    List<ProductEntity> findByCategoryIgnoreCase(String category);

    List<ProductEntity> findTop1ByCategoryOrderByIdAsc(String category);

    List<ProductEntity> findTop6ByOrderByCreatedAtDesc();

    List<ProductEntity> findTop1ByCategoryOrderByCreatedAtDesc(String category);


    List<ProductEntity> findByPriceBetween(BigDecimal min, BigDecimal max);

}
