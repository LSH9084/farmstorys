package com.farmstory.requestdto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class DeleteUserReqDto {
    private List<String> userIdx;
}
