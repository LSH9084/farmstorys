package com.farmstory.requestdto.product;

import com.farmstory.entity.product.ProductEntity;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostProductDto {
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

    public ProductEntity toEntity() {
        return ProductEntity.builder()
                .prodName(prodName)
                .prodType(prodType)
                .prodDelivery(prodDelivery)
                .prodPrice(prodPrice)
                .prodDiscount(prodDiscount)
                .prodSavePoint(prodSavePoint)
                .prodStock(prodStock)
                .prodEtc(prodEtc)
                .build();
    }
}
