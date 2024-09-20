package com.farmstory.repository.board;

import com.farmstory.entity.board.BoardEntity;
import com.farmstory.entity.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    BoardEntity findByUser(UserEntity user);

    Page<BoardEntity> findAllByBoardTypeAndBoardSection(Pageable pageable, String type, String section);
}
