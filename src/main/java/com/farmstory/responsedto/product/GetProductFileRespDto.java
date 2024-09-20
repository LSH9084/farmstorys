package com.farmstory.responsedto.product;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class GetProductFileRespDto {
    private String filename;
    private String path;
    private String type;
}
