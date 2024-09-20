package com.farmstory.entity.board;

import com.farmstory.responsedto.board.GetBoardFileRespDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "board_file")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BoardFileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_file_idx")
    private Long boardFileIdx;

    @Column(name = "filename", nullable = false)
    private String filename;

    @Column(name = "filepath", nullable = false)
    private String filepath;

    @Column(name = "download_cnt")
    private int downloadCnt;

    @ManyToOne
    @JoinColumn(name = "board_idx")
    private BoardEntity board;

    public GetBoardFileRespDto toGetBoardFileRespDto() {
        return GetBoardFileRespDto.builder()
                .filePath(filepath)
                .fileName(filename)
                .downloadCnt(downloadCnt)
                .fileIdx(boardFileIdx)
                .build();
    }
}
