package com.farmstory.entity.board;

import com.farmstory.entity.user.UserEntity;
import com.farmstory.responsedto.board.GetBoardFileRespDto;
import com.farmstory.responsedto.board.GetBoardRespDto;
import com.farmstory.responsedto.board.GetBoardsRespDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Table(name = "board")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_idx")
    private Long boardIdx;

    @Column(name = "board_title", nullable = false, length = 50)
    private String boardTitle;

    @Column(name = "board_content", nullable = false ,columnDefinition = "TEXT")
    private String boardContent;

    @Column(name = "board_view_cnt", nullable = false)
    private Integer boardViewCnt ;

    @Column(name = "board_section", nullable = false, length = 20)
    private String boardSection;

    @Column(name = "board_type", nullable = false, length = 20)
    private String boardType;

    @Column(name = "board_create_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp boardCreateAt;

    @Column(name = "board_modify_at", nullable = false)
    @UpdateTimestamp
    private Timestamp boardModifyAt;

    @ManyToOne
    @JoinColumn(name = "user_idx")
    private UserEntity user;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<CommentEntity> comments ;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<BoardFileEntity> boardFiles ;


    public GetBoardsRespDto toGetBoardsRespDto(String userId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String owner;

        if(user.getUserId().equals(userId)) {
            owner = "true";
        } else {
            owner = "false";
        }

        // LocalDateTime으로 변환하고 포맷 적용
        String formattedBoardModifyAt = boardModifyAt.toLocalDateTime().format(formatter);
        String formattedBoardCreateAt = boardCreateAt.toLocalDateTime().format(formatter);

        return GetBoardsRespDto.builder()
                .boardIdx(boardIdx)
                .boardModifyAt(formattedBoardModifyAt)
                .boardCreateAt(formattedBoardCreateAt)
                .boardViewCnt(boardViewCnt)
                .boardTitle(boardTitle)
                .boardCommentCnt(comments.size())
                .userName(user.getUserName())
                .owner(owner)
                .build();
    }
    public GetBoardRespDto toGetBoardRespDto() {
        List<GetBoardFileRespDto> files = boardFiles.stream()
                .map(BoardFileEntity::toGetBoardFileRespDto)
                .toList();

        return GetBoardRespDto.builder()
                .userId(user.getUserId())
                .boardTitle(boardTitle)
                .boardContent(boardContent)
                .boardIdx(boardIdx)
                .userNick(user.getUserNick())
                .files(files)
                .build();
    }
}
