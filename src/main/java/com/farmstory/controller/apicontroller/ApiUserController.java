package com.farmstory.controller.apicontroller;

import com.farmstory.requestdto.user.PutMypageUserReqDto;
import com.farmstory.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ApiUserController {

    private final UserService userService;

//    @DeleteMapping("/user")
//    public ResponseEntity<String> deleteUser(
//            Long userIdx
//    ) {
//        String path = "http://localhost:8080/";
//        return ResponseEntity.ok().body(path);
//    }

    @PutMapping("/user")
    public ResponseEntity<String> updateUser(
            @RequestBody PutMypageUserReqDto request
    ){
        userService.updateUser(request);

        return ResponseEntity.ok().body("SU");
    }

    @DeleteMapping("/user")
    public ResponseEntity<String> deleteUser(HttpServletRequest request, HttpServletResponse response) {
        userService.deleteUserAndLogout(request, response); // 서비스 메서드 호출

        return ResponseEntity.ok().body("SU"); // 상태 204 (No Content) 응답
    }
}
