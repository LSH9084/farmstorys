package com.farmstory.responsedto.board;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class GetBoardFileRespDto {
    private String fileName;
    private String filePath;
    private int downloadCnt;
    private Long fileIdx;
}
