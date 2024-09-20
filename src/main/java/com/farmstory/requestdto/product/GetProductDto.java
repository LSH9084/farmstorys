package com.farmstory.requestdto.product;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetProductDto {
    private Long prodIdx;

    private String prodName;

    private String prodType;

    private BigDecimal prodDelivery;

    private BigDecimal prodPrice;

    private BigDecimal prodDiscount;

    private BigDecimal prodSavePoint;

    private Integer prodStock;

    private Timestamp prodCreateAt;

    private String prodEtc;

    private String prodFileName;

    private String prodFilePath;

}
