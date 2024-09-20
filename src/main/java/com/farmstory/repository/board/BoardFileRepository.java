package com.farmstory.repository.board;

import com.farmstory.entity.board.BoardEntity;
import com.farmstory.entity.board.BoardFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardFileRepository extends JpaRepository<BoardFileEntity,Long> {
    void deleteByBoard(BoardEntity board);

    List<BoardFileEntity> findAllByBoard(BoardEntity boardEntity2);
}
