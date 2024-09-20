package com.farmstory.mapper;

import com.farmstory.entity.user.UserEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestUserMapper {

    @Insert("INSERT INTO users (user_id, user_pwd, user_name, user_nick, user_email, user_hp, user_role) " +
            "VALUES (#{userId}, #{userPwd}, #{userName}, #{userNick}, #{userEmail}, #{userHp}, #{userRole})")
    void insertUser(UserEntity user);

}
