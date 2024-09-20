package com.farmstory.requestdto.cart;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PostCartItemReqDto {
    private int prodIdx;
    private int cartItemQuantity;
}
