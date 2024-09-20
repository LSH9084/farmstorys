package com.farmstory.requestdto.user;

import com.farmstory.entity.user.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

@Getter
@Setter
@Builder
public class SignupUserDto {

    private Long userIdx;

    private String userId;

    private String userPwd;

    private String userName;

    private String userNick;

    private String userEmail;

    private String userHp;

    private Date userCreatedAt;

    private String userRole ;

    public UserEntity dtoToEntity(PasswordEncoder passwordEncoder) {
        return UserEntity.builder()
                .userId(userId)
                .userPwd(passwordEncoder.encode(userPwd))
                .userName(userName)
                .userNick(userNick)
                .userEmail(userEmail)
                .userHp(userHp)
                .build();
    }
}
