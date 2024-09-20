package com.farmstory.responsedto.user;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetUsersRespDto {
    private Long userIdx;

    private String userId;

    private String userPwd;

    private String userName;

    private String userNick;

    private String userEmail;

    private String userHp;

    private Date userCreateAt;

    private String userRole ;
}
