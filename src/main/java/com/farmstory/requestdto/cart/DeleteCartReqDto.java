package com.farmstory.requestdto.cart;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class DeleteCartReqDto {
    private List<String> cartItemIdx;
}
