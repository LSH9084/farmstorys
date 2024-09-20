package com.farmstory.requestdto.user;

import com.farmstory.entity.user.UserAddressEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SignupUserAddressDto {
    private Long userIdx;

    private String addrZone;

    private String addr;

    private String addrDetail;

    public UserAddressEntity toEntity(Long userIdx) {
        return UserAddressEntity.builder()
                .userIdx(userIdx)
                .addr(addr)
                .addrDetail(addrDetail)
                .addrZone(addrZone)
                .build();
    }
}
