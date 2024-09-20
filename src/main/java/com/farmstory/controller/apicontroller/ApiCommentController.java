package com.farmstory.controller.apicontroller;

import com.farmstory.requestdto.board.PostCommentReqDto;
import com.farmstory.requestdto.board.PutCommentReqDto;
import com.farmstory.responsedto.board.GetCommentsRespDto;
import com.farmstory.service.board.CommentService;
import com.farmstory.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ApiCommentController {

    private final CommentService commentService;
    private final UserService userService;

    @GetMapping("/comments")
    public ResponseEntity<Map<String,Object>> getComments(
            @RequestParam Long boardIdx
    ) {
        Map<String,Object> response = new HashMap<>();

        List<GetCommentsRespDto> comments = commentService.getComments(boardIdx);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if(comments==null){
            response.put("empty","EMC");
        }
        response.put("comments", comments);
        response.put("boardIdx", boardIdx);

        return ResponseEntity.ok().body(response);

    }

    @PostMapping("/comment")
    public ResponseEntity<String> postComment(
            @RequestBody PostCommentReqDto request
    ){

        commentService.postComment(request);


        String path = "api/client/comments?boardIdx="+request.getBoardIdx();

        return ResponseEntity.ok(path);
    }

    @PutMapping("/comment")
    public ResponseEntity<String> putComment(
            @RequestBody PutCommentReqDto request
    ){
        commentService.updateComment(request);

        String path = "api/client/comments?boardIdx=boardIdx";

        return ResponseEntity.ok().body(path);
    }

    @DeleteMapping("/comment")
    public ResponseEntity<String> deleteComment(
            @RequestParam Long commentIdx
    ){
        commentService.deleteComment(commentIdx);

        String path = "api/client/comments?boardIdx=";

        return ResponseEntity.ok().body(path);
    }
}
