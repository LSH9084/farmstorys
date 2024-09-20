package com.farmstory.responsedto.board;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class GetBoardsRespDto {

    private long boardIdx;
    private String boardTitle;
    private String userName;
    private String boardCreateAt;
    private String boardModifyAt;
    private int boardViewCnt;
    private int boardCommentCnt;
    private String owner;
}
