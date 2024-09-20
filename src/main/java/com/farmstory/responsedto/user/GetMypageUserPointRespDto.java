package com.farmstory.responsedto.user;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class GetMypageUserPointRespDto {
    private String detail;
    private BigDecimal savePoint;
    private BigDecimal usePoint;
}
