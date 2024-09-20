package com.farmstory.repository.product;

import com.farmstory.entity.product.ProductSnapShotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSnapShotRepository extends JpaRepository<ProductSnapShotEntity, Long> {
}
