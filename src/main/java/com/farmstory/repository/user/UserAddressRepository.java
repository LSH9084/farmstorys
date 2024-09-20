package com.farmstory.repository.user;

import com.farmstory.entity.user.UserAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddressEntity,Long> {
    UserAddressEntity findByUserIdx(Long userIdx);

    void deleteByUserIdx(Long userIdx);
}
