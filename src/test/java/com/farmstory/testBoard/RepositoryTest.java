package com.farmstory.testBoard;

import com.farmstory.entity.board.BoardEntity;
import com.farmstory.entity.board.BoardFileEntity;
import com.farmstory.entity.board.CommentEntity;
import com.farmstory.entity.user.UserEntity;
import com.farmstory.repository.board.BoardFileRepository;
import com.farmstory.repository.board.BoardRepository;
import com.farmstory.repository.board.CommentRepository;
import com.farmstory.repository.user.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
public class RepositoryTest {

    @Autowired BoardRepository boardRepository;
    @Autowired
    BoardFileRepository boardFileRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    UserRepository userRepository;


    @Test
    void insertBoard(){
        UserEntity user = UserEntity.builder()
                .userHp("ㅇㄴㅇ")
                .userId("21232")
                .userEmail("ㅁㄴㅇㄴㅁㅇ")
                .userName("ㅇㄴㅁ")
                .userNick("ㅁㄴㅇㅁㄴㅇ")
                .userPwd("1515")
                .build();

        userRepository.save(user);

        BoardEntity board = BoardEntity.builder()
                .user(user)
                .boardContent("ddd")
                .boardSection("ddd")
                .boardTitle("wpahr")
                .boardType("type")
                .build();

        boardRepository.save(board);
    }

    @Test
    void insertBoardFile(){
        BoardEntity board = BoardEntity.builder()
                .boardIdx((long)(4))
                .build();

        BoardFileEntity boardFile = BoardFileEntity.builder()
                .board(board)
                .filename("ddd")
                .filepath("dddd")
                .build();

        boardFileRepository.save(boardFile);
    }

    @Test
    void insertComment(){
        UserEntity user = UserEntity.builder()
                .userIdx((long)1)
                .build();

        BoardEntity board = BoardEntity.builder()
                .boardIdx((long)(4))
                .build();

        CommentEntity comment = CommentEntity.builder()
                .board(board)
                .commentContent("ddd")
                .user(user)
                .build();

        commentRepository.save(comment);
    }

    @Test
    @Transactional
    void selectComments(){
        List<CommentEntity> comments = commentRepository.findAll();
        System.out.println(comments);

    }

    @Test
    
    void updateComment(){
        UserEntity user = UserEntity.builder()
                .userIdx((long)2)
                .build();

        BoardEntity board = BoardEntity.builder()
                .boardIdx((long)(3))
                .user(user)
                .build();

        CommentEntity comment = CommentEntity.builder()
                .commentIdx((long)2)
                .board(board)
                .user(user)
                .commentContent("수정함")
                .build();
        commentRepository.save(comment);
    }

    @Test
    @Transactional
    @Rollback(false)
    void deleteBoard(){


        BoardEntity board = BoardEntity.builder()
                .boardIdx((long)(5))
                .build();

        boardRepository.deleteById(board.getBoardIdx());

    }

    @Autowired
    private EntityManager entityManager;

    @Test
    @Transactional
    @Rollback(false)
    void updateBoard(){
        UserEntity user = UserEntity.builder()
                .userIdx((long)2)
                .build();
        BoardEntity boardId = BoardEntity.builder()
                .user(user)
                .boardTitle("수정제목")
                .boardContent("수정함").boardSection("croptalk").boardType("school").boardIdx((long)5).build();
        if(boardId != null){
            boardRepository.save(boardId);
        }


    }

}
