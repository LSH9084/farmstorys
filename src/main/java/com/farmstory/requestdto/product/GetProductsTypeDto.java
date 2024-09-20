package com.farmstory.requestdto.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetProductsTypeDto {
    private String prodFileName;
    private String prodFilePath;
    private String prodType;
    private String prodName;
}
