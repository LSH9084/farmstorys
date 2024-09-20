package com.farmstory.requestdto.product;

import com.farmstory.entity.product.ProductFileEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostProductFileDto {
    private Long prodFileIdx;

    private String prodFileType;

    private String prodFileName;

    private String prodFilePath;

    public ProductFileEntity toEntity() {
        return ProductFileEntity.builder()
                .prodFileName(prodFileName)
                .prodFilePath(prodFilePath)
                .prodFileType(prodFileType)
                .build();
    }
}
