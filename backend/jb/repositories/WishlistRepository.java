package com.jb.jb.repositories;

import com.jb.jb.entities.User;
import com.jb.jb.entities.Wishlist;
import com.jb.jb.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByUser(User user);
    boolean existsByUserAndProduct(User user, ProductEntity product);
}
