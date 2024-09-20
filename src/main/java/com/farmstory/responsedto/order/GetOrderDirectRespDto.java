package com.farmstory.responsedto.order;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class GetOrderDirectRespDto {
    private Long prodIdx;
    private String prodName;
    private BigDecimal prodPrice;
    private BigDecimal prodDelivery;
    private int orderItemQuantity;
    private BigDecimal userPoint;
    private String fileName;
    private String filePath;
    private BigDecimal totalPrice;
    private BigDecimal orderSavePoint;


}
