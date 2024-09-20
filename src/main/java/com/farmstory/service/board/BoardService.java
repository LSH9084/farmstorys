package com.farmstory.service.board;

import com.farmstory.entity.board.BoardEntity;
import com.farmstory.entity.board.BoardFileEntity;
import com.farmstory.entity.user.UserEntity;
import com.farmstory.repository.board.BoardFileRepository;
import com.farmstory.repository.board.BoardRepository;
import com.farmstory.repository.user.UserRepository;
import com.farmstory.requestdto.board.PostBoardReqDto;
import com.farmstory.requestdto.board.PutBoardReqDto;
import com.farmstory.responsedto.board.GetBoardRespDto;
import com.farmstory.responsedto.board.GetBoardsRespDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;
    private final UserRepository userRepository;

    public Page<GetBoardsRespDto> selectBoards(int page,String section,String type) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();



        Pageable pageable = PageRequest.of(page, 8);

        Page<BoardEntity> boardEntities = boardRepository.findAllByBoardTypeAndBoardSection(pageable,type,section);

        List<BoardEntity> boardsStm = boardEntities.getContent();

//
        int total = boardsStm.size();

        if(boardsStm.isEmpty()){
            return null;
        }
        List<GetBoardsRespDto> boardDtos = boardsStm.stream()
                        .map(v->v.toGetBoardsRespDto(username))
                        .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                            Collections.reverse(list);
                            return list.stream();
                        })).collect(Collectors.toList());

        return new PageImpl<>(boardDtos, pageable, boardEntities.getTotalElements());
    }

    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostConstruct
    public void init() {
        // 파일 업로드 디렉토리가 존재하는지 확인하고 없으면 생성합니다
        Path path = Paths.get(uploadDir);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new RuntimeException("Could not create upload directory", e);
            }
        }
    }

    public String saveFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Cannot store empty file");
        }
        String originalFilename = file.getOriginalFilename();

        String extension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";

        String randomFilename = UUID.randomUUID().toString() + extension;
        // 파일이 저장될 경로를 생성합니다
        Path path = Paths.get(uploadDir).resolve(randomFilename);

        // 파일을 지정된 경로에 저장합니다
        Files.copy(file.getInputStream(), path);

        return path.toString();
    }

    @Transactional
    public String insertBoard(PostBoardReqDto boardRequest, String path1, String path2) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntity userEntity = userRepository.findByUserId(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));


        BoardEntity boardEntity = boardRequest.toBoardEntity(userEntity);
        BoardEntity boardEntity2 = boardRepository.save(boardEntity);

        if(path1!=null && !path1.isEmpty()) {
            BoardFileEntity boardFileEntity = BoardFileEntity.builder()
                    .board(boardEntity2)
                    .filepath("/file/")
                    .downloadCnt(0)
                    .filename(path1.substring(26))
                    .build();

            boardFileRepository.save(boardFileEntity);
        }
        if(path2!=null && !path2.isEmpty()) {
            BoardFileEntity boardFileEntity = BoardFileEntity.builder()
                    .board(boardEntity2)
                    .downloadCnt(0)
                    .filepath("/file/")
                    .filename(path2.substring(26))
                    .build();

            boardFileRepository.save(boardFileEntity);
        }

        return "SU";
    }

    public GetBoardRespDto getBoard(int boardIdx) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<BoardEntity> optBoard = boardRepository.findById((long)boardIdx);
        GetBoardRespDto boardRespDto;
        if(optBoard.isEmpty()) {
            return null;
        }
        BoardEntity newBoard = BoardEntity.builder()
                .boardViewCnt(optBoard.get().getBoardViewCnt()+1)
                .boardContent(optBoard.get().getBoardContent())
                .boardTitle(optBoard.get().getBoardTitle())
                .boardSection(optBoard.get().getBoardSection())
                .boardType(optBoard.get().getBoardType())
                .boardCreateAt(optBoard.get().getBoardCreateAt())
                .boardIdx(optBoard.get().getBoardIdx())
                .user(optBoard.get().getUser())
                .boardFiles(optBoard.get().getBoardFiles())
                .build();

        boardRepository.save(newBoard);

        boardRespDto = newBoard.toGetBoardRespDto();

        return boardRespDto;
    }

    @Transactional
    public String downloadBoard(long boardFileIdx) {

        Optional<BoardFileEntity> boardFileEntity = boardFileRepository.findById(boardFileIdx);

        if(boardFileEntity.isEmpty()) {
            return "BNF";
        }

        BoardFileEntity newBoard = BoardFileEntity.builder()
                .downloadCnt(boardFileEntity.get().getDownloadCnt()+1)
                .filename(boardFileEntity.get().getFilename())
                .filepath(boardFileEntity.get().getFilepath())
                .boardFileIdx(boardFileEntity.get().getBoardFileIdx())
                .board(boardFileEntity.get().getBoard())
                .build();

        boardFileRepository.save(newBoard);

        String fileName = boardFileEntity.get().getFilename();



        return fileName;
    }

    @Transactional
    public void deleteBoard(Long boardIdx) {
        Optional<BoardEntity> optBoard = boardRepository.findById(boardIdx);
        if(optBoard.isEmpty()) {
            return;
        }

        boardRepository.delete(optBoard.get());

    }

    @Transactional
    public void updateBoard(PutBoardReqDto boardRequest, String path1, String path2) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntity userEntity = userRepository.findByUserId(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        BoardEntity boardEntity = boardRequest.toBoardEntity(userEntity);
        Optional<BoardEntity> optBoard = boardRepository.findById(boardRequest.getBoardIdx());
        if(optBoard.isEmpty()){
            return;
        }

        BoardEntity boardEntity2 = BoardEntity.builder()
                .boardContent(boardRequest.getBoardContent())
                .boardTitle(boardRequest.getBoardTitle())
                .boardSection(optBoard.get().getBoardSection())
                .boardType(optBoard.get().getBoardType())
                .boardViewCnt(optBoard.get().getBoardViewCnt())
                .boardCreateAt(optBoard.get().getBoardCreateAt())
                .boardIdx(optBoard.get().getBoardIdx())
                .user(userEntity)
                .build();

        boardRepository.save(boardEntity2);

        List<BoardFileEntity> files = boardFileRepository.findAllByBoard(boardEntity2);
        if(files.isEmpty()){
            if(path1!=null) {
                BoardFileEntity boardFileEntity = BoardFileEntity.builder()
                        .board(boardEntity2)
                        .filepath("/file/")
                        .downloadCnt(0)
                        .filename(path1.substring(26))
                        .build();

                boardFileRepository.save(boardFileEntity);
            }
            if(path2!=null) {
                BoardFileEntity boardFileEntity = BoardFileEntity.builder()
                        .board(boardEntity2)
                        .filepath("/file/")
                        .downloadCnt(0)
                        .filename(path2.substring(26))
                        .build();
                boardFileRepository.save(boardFileEntity);
            }
        }
        if(files.size()==1){
            if(path1!=null) {
                BoardFileEntity boardFileEntity = files.get(0);
                BoardFileEntity updateFile = BoardFileEntity.builder()
                        .boardFileIdx(boardFileEntity.getBoardFileIdx())
                        .filepath("/file/")
                        .downloadCnt(0)
                        .filename(path1.substring(26))
                        .board(boardEntity2)
                        .build();

                boardFileRepository.save(updateFile);
            } else {
                BoardFileEntity boardFileEntity = files.get(0);
                boardFileRepository.deleteById(boardFileEntity.getBoardFileIdx());
            }
            if(path2!=null) {
                BoardFileEntity uploadFile = BoardFileEntity.builder()
                        .filepath("/file/")
                        .downloadCnt(0)
                        .filename(path2.substring(26))
                        .board(boardEntity2)
                        .build();

                boardFileRepository.save(uploadFile);
            }
        }
        if(files.size()==2){
            if(path1!=null) {
                BoardFileEntity boardFileEntity = files.get(0);
                BoardFileEntity updateFile = BoardFileEntity.builder()
                        .boardFileIdx(boardFileEntity.getBoardFileIdx())
                        .filepath("/file/")
                        .downloadCnt(0)
                        .filename(path1.substring(26))
                        .board(boardEntity2)
                        .build();

                boardFileRepository.save(updateFile);
            } else {
                BoardFileEntity boardFileEntity = files.get(0);
                boardFileRepository.deleteById(boardFileEntity.getBoardFileIdx());
            }
            if(path2!=null) {
                BoardFileEntity boardFileEntity = files.get(1);
                BoardFileEntity updateFile = BoardFileEntity.builder()
                        .boardFileIdx(boardFileEntity.getBoardFileIdx())
                        .filepath("/file/")
                        .downloadCnt(0)
                        .filename(path1.substring(26))
                        .board(boardEntity2)
                        .build();

                boardFileRepository.save(updateFile);
            } else {
                BoardFileEntity boardFileEntity = files.get(1);
                boardFileRepository.deleteById(boardFileEntity.getBoardFileIdx());
            }
        }

    }
}

