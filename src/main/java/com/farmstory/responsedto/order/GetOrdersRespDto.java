package com.farmstory.responsedto.order;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class GetOrdersRespDto {
    private Long orderIdx;
    private int prodQuantity;
    private String userName;
    private Date orderCreateAt;
    private BigDecimal totalPrice;
    private String prodName;
    private BigDecimal prodPrice;
    private String fileName;
    private String filePath;
    private BigDecimal prodDelivery;
    private int itemQuantity;
}
