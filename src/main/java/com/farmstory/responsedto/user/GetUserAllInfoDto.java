package com.farmstory.responsedto.user;

import com.farmstory.entity.user.UserAddressEntity;
import com.farmstory.entity.user.UserEntity;
import com.farmstory.entity.user.UserPointEntity;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetUserAllInfoDto {
    private Long userIdx;

    private String userId;

    private String userName;

    private String userNick;

    private String userEmail;

    private String userHp;

    private Date userCreateAt;

    private String userRole ;

    private String addrZone;

    private String addr;

    private String addrDetail;

    private BigDecimal userPoint;

    public GetUserAllInfoDto(UserEntity user, UserAddressEntity userAddr, UserPointEntity userPoint) {
        this.userIdx = user.getUserIdx();
        this.userId = user.getUserId();
        this.userEmail = user.getUserEmail();
        this.userHp = user.getUserHp();
        this.userName = user.getUserName();
        this.userNick = user.getUserNick();
        this.userCreateAt = user.getUserCreateAt();
        this.userRole = user.getUserRole();
        this.addrZone = userAddr.getAddrZone();
        this.addr = userAddr.getAddr();
        this.addrDetail = userAddr.getAddrDetail();
        this.userPoint = userPoint.getUserPoint();
    }

    public GetUserAllInfoDto(UserEntity user, UserAddressEntity userAddr) {
        this.userIdx = user.getUserIdx();
        this.userId = user.getUserId();
        this.userEmail = user.getUserEmail();
        this.userHp = user.getUserHp();
        this.userName = user.getUserName();
        this.userNick = user.getUserNick();
        this.userCreateAt = user.getUserCreateAt();
        this.addrZone = userAddr.getAddrZone();
        this.addr = userAddr.getAddr();
        this.addrDetail = userAddr.getAddrDetail();
    }
}
