package com.farmstory.repository.user;

import com.farmstory.entity.user.UserPointDetailEntity;
import com.farmstory.entity.user.UserPointEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPointDetailRepository extends JpaRepository<UserPointDetailEntity,Long> {

    List<UserPointDetailEntity> findAllByPoint(UserPointEntity userPointEntity);

    Page<UserPointDetailEntity> findAllByPoint(UserPointEntity userPointEntity, Pageable pageable);
}
