package com.farmstory.responsedto.board;

import lombok.*;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class GetBoardRespDto {
    private String boardTitle;
    private String boardContent;
    private String userId;
    private String userNick;
    private Long boardIdx;
    List<GetBoardFileRespDto> files;
}
