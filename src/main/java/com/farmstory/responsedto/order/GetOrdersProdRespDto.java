package com.farmstory.responsedto.order;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class GetOrdersProdRespDto {
    private String prodName;
    private BigDecimal prodPrice;
    private String fileName;
    private String filePath;
}
