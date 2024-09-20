package com.farmstory.responsedto.cart;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class GetCartsRespDto {
    private long cartItemIdx;
    private int cartItemQuantity;
    private BigDecimal prodDiscount;
    private BigDecimal prodPoint;
    private BigDecimal prodPrice;
    private BigDecimal prodTotal;
    private String prodFilePath;
    private String prodFileName;
    private String prodName;
    private String prodType;
    private BigDecimal prodDelivery;
    private long cartIdx;
    private String userName;
    private String userHp;
    private BigDecimal userPoint;

}
