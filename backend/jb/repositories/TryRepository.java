package com.jb.jb.repositories;

import com.jb.jb.entities.TryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TryRepository extends JpaRepository<TryEntity,Long> {
    boolean existsBySku(String sku);
}
