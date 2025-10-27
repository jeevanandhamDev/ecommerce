package com.jb.jb.repositories;

import com.jb.jb.entities.LimitedEditionEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LimitedEditionRepository extends JpaRepository<LimitedEditionEntity,Long> {
    boolean existsBySku(String sku);
}
