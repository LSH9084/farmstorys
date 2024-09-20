package com.farmstory.requestdto.board;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PutBoardFileReqDto {
    private Long boardFileIdx;

    private String boardFileName;

    private String boardFilePath;
}
