package com.farmstory.entity.board;

import com.farmstory.entity.user.UserEntity;
import com.farmstory.responsedto.board.GetCommentsRespDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "comment")
@NoArgsConstructor
@Getter
@AllArgsConstructor
@Builder
@ToString
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_idx")
    private Long commentIdx;

    @Column(name = "comment_content", nullable = false,columnDefinition = "TEXT")
    private String commentContent;

    @Column(name = "comment_create_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp commentCreateAt;

    @UpdateTimestamp
    @Column(name = "comment_modify_at", nullable = false)
    private Timestamp commentModifyAt;

    @ManyToOne
    @JoinColumn(name = "board_idx")
    private BoardEntity board;

    @ManyToOne
    @JoinColumn(name = "user_idx")
    private UserEntity user;

    public GetCommentsRespDto toGetCommentsRespDto(String userNick, String role) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedBoardModifyAt = commentModifyAt.toLocalDateTime().format(formatter);
        String formattedBoardCreateAt = commentCreateAt.toLocalDateTime().format(formatter);

        String checker;
        if(userNick.equals(user.getUserNick())){
            checker = "true";
        } else {
            checker = "false";
        }
        String writer;
        if(user.getUserNick().equals(board.getUser().getUserNick())){
            writer = "true";
        } else {
            writer = "false";
        }
        String admin;
        if(user.getUserRole().equals("admin")){
            admin = "true";
        } else {
            admin = "false";
        }
        return GetCommentsRespDto.builder()
                .commentContent(commentContent)
                .userNick(user.getUserNick())
                .commentIdx(commentIdx)
                .userId(user.getUserId())
                .commentCreateAt(formattedBoardCreateAt)
                .commentModifyAt(formattedBoardModifyAt)
                .boardIdx(board.getBoardIdx())
                .equalsIdx(checker)
                .writer(writer)
                .admin(admin)
                .role(role)
                .build();
    }
}
