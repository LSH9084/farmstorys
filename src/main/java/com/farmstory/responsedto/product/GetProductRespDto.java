package com.farmstory.responsedto.product;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class GetProductRespDto {
    private Long prodIdx;

    private String prodName;

    private BigDecimal prodDelivery;

    private BigDecimal prodPrice;

    private BigDecimal prodDiscount;

    private BigDecimal prodSavePoint;

    private int prodStock;

    private Timestamp prodCreateAt;

    private String prodEtc;

    List<GetProductFileRespDto> files;

}
