package com.farmstory.requestdto.board;

import com.farmstory.entity.board.BoardEntity;
import com.farmstory.entity.user.UserEntity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PutBoardReqDto {
    private String boardTitle;
    private String boardContent;
    private Long boardIdx;

    public BoardEntity toBoardEntity(UserEntity user) {
        return BoardEntity.builder()
                .boardTitle(boardTitle)
                .boardContent(boardContent)
                .boardIdx(boardIdx)
                .user(user)
                .build();
    }
}
