package com.farmstory.service.board;

import com.farmstory.entity.board.BoardEntity;
import com.farmstory.entity.board.CommentEntity;
import com.farmstory.entity.user.UserEntity;
import com.farmstory.repository.board.BoardRepository;
import com.farmstory.repository.board.CommentRepository;
import com.farmstory.repository.user.UserRepository;
import com.farmstory.requestdto.board.PostCommentReqDto;
import com.farmstory.requestdto.board.PutCommentReqDto;
import com.farmstory.responsedto.board.GetCommentsRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public List<GetCommentsRespDto> getComments(Long boardIdx) {

        Optional<BoardEntity> board = boardRepository.findById(boardIdx);
        if(board.isEmpty()){
            return null;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntity userEntity = userRepository.findByUserId(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        String role;
        if(userEntity.getUserRole().equals("admin")){
            role = "true";
        } else {
            role = "false";
        }

        List<CommentEntity> commentEntities = commentRepository.findAllByBoard(board.get());
        if(commentEntities.isEmpty()){
            return null;
        }
        List<GetCommentsRespDto> commentRespDtos = commentEntities.stream()
                .map(v->v.toGetCommentsRespDto(userEntity.getUserNick(),role)).collect(Collectors.toList());



        return commentRespDtos;
    }

    public void postComment(PostCommentReqDto request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntity userEntity = userRepository.findByUserId(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Optional<BoardEntity> optBoard = boardRepository.findById(request.getBoardIdx());
        if(optBoard.isEmpty()){
            return;
        }

        Optional<UserEntity> optUser = userRepository.findByUserId(username);
        if(optUser.isEmpty()){
            return;
        }
        CommentEntity commentEntity = CommentEntity.builder()
                .commentContent(request.getCommentContent())
                .board(optBoard.get())
                .user(optUser.get())
                .build();

        commentRepository.save(commentEntity);
    }

    public void updateComment(PutCommentReqDto request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntity userEntity = userRepository.findByUserId(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Optional<CommentEntity> optComment = commentRepository.findById(request.getCommentIdx());
        if(optComment.isEmpty()){
            return;
        }

        Optional<BoardEntity> optBoard = boardRepository.findById(optComment.get().getBoard().getBoardIdx());
        if(optBoard.isEmpty()){
            return;
        }

        CommentEntity commentEntity = CommentEntity.builder()
                .commentContent(request.getCommentContent())
                .board(optBoard.get())
                .commentCreateAt(optComment.get().getCommentCreateAt())
                .user(userEntity)
                .commentIdx(request.getCommentIdx())
                .build();

        commentRepository.save(commentEntity);

    }

    public void deleteComment(Long commentIdx) {

        commentRepository.deleteById(commentIdx);
    }
}
