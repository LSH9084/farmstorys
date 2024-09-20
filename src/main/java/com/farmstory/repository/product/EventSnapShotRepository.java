package com.farmstory.repository.product;

import com.farmstory.entity.product.EventSnapShotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventSnapShotRepository extends JpaRepository<EventSnapShotEntity,Long> {
}
