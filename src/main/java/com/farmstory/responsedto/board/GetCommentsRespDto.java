package com.farmstory.responsedto.board;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class GetCommentsRespDto {
    private Long commentIdx;
    private String userNick;
    private String commentCreateAt;
    private String commentModifyAt;
    private String commentContent;
    private String userId;
    private String equalsIdx;
    private Long boardIdx;
    private String writer;
    private String admin;
    private String role;
}
