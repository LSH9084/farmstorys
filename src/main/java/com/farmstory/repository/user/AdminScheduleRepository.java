package com.farmstory.repository.user;

import com.farmstory.entity.user.AdminScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminScheduleRepository extends JpaRepository<AdminScheduleEntity, Long> {
    List<AdminScheduleEntity> findAllByText(String s);
}
