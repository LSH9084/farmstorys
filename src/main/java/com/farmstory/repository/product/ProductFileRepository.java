package com.farmstory.repository.product;

import com.farmstory.entity.product.ProductEntity;
import com.farmstory.entity.product.ProductFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductFileRepository extends JpaRepository<ProductFileEntity,Long> {
    List<ProductFileEntity> findAllByProd(ProductEntity productEntity);
}
