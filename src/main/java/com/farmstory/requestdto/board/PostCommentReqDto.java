package com.farmstory.requestdto.board;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PostCommentReqDto {
    private Long boardIdx;
    private String commentContent;
}
