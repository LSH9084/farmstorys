package com.farmstory.repository.user;

import com.farmstory.entity.user.UserScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserScheduleRepository extends JpaRepository<UserScheduleEntity, Long> {
    List<UserScheduleEntity> findAllByUserIdx(Long userIdx);

}
