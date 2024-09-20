package com.farmstory.repository.user;

import com.farmstory.entity.user.UserPointEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPointRepository extends JpaRepository<UserPointEntity,Long> {
    Optional<UserPointEntity> findByUserIdx(Long userIdx);
}
