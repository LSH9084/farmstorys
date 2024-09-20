package com.farmstory.requestdto.user;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PutMypageUserReqDto {
    private String userName;
    private String userHp;
    private String userNick;
    private String userEmail;
    private String addrZone;
    private String addr;
    private String addrDetail;

}
