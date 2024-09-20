package com.farmstory.repository.cart;

import com.farmstory.entity.cart.CartEntity;
import com.farmstory.entity.cart.CartItemEntity;
import com.farmstory.entity.product.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity,Long> {
    List<CartItemEntity> findAllByCart(CartEntity cartEntity);

    Optional<CartItemEntity> findByCartAndProd(CartEntity cartEntity, ProductEntity productEntity);
}
