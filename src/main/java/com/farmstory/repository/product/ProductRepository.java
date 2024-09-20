package com.farmstory.repository.product;

import com.farmstory.entity.product.ProductEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity,Long> {
    @Override
    Page<ProductEntity> findAll(Pageable pageable);
    Page<ProductEntity> findAllByProdType(Pageable pageable, String prodType);
    @Query("SELECT COUNT(p) FROM ProductEntity p WHERE p.prodType = :type")
    long countByType(@Param("type") String type);

    List<ProductEntity> findAllByProdType(String type);
}
