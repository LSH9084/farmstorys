package com.farmstory.requestdto.user;

import com.farmstory.entity.user.UserEntity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PutUserRoleReqDto {
    private Long userIdx;
    private String userRole;

    public UserEntity toEntity(){
        return UserEntity.builder()
                .userIdx(userIdx)
                .userRole(userRole)
                .build();
    }
}
