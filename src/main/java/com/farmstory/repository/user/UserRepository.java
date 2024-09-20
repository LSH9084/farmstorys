package com.farmstory.repository.user;

import com.farmstory.entity.user.UserEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

    public Optional<UserEntity> findByUserId(String userId);
    public Page<UserEntity> findAll(Pageable pageable);

    @Modifying
    @Query("UPDATE UserEntity u SET u.userRole = :userRole WHERE u.userIdx = :userIdx")
    int updateUserRole(@Param("userRole") String userRole, @Param("userIdx") Long userIdx);

    Optional<UserEntity> findByUserNick(String userNick);

    Optional<UserEntity> findByUserEmail(String userEmail);
}
