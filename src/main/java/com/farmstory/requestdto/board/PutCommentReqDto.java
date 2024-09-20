package com.farmstory.requestdto.board;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PutCommentReqDto {
    private Long commentIdx;
    private String commentContent;
}
