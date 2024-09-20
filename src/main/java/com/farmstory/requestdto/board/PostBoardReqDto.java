package com.farmstory.requestdto.board;

import com.farmstory.entity.board.BoardEntity;
import com.farmstory.entity.user.UserEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PostBoardReqDto {
    @NotBlank(message = "Board title must not be blank")
    private String boardTitle;
    @NotBlank(message = "Board content must not be blank")
    private String boardContent;
    private String section;
    private String type;

    public BoardEntity toBoardEntity(UserEntity user) {
        return BoardEntity.builder()
                .boardTitle(boardTitle)
                .boardContent(boardContent)
                .boardSection(section)
                .boardType(type)
                .user(user)
                .boardViewCnt(0)
                .build();
    }
}
