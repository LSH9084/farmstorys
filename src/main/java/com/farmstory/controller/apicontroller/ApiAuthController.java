package com.farmstory.controller.apicontroller;

import com.farmstory.requestdto.user.PutUserReqDto;
import com.farmstory.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class ApiAuthController {

    private final UserService userService;


    @GetMapping("/check/id")
    public ResponseEntity<String> checkId(
            @RequestParam String userId
    ){
        if(userId.contains(" ")) {
            return ResponseEntity.ok().body("EW"); // 공백이 포함된 경우의 응답 코드 예
        }

        if(userId.isEmpty()){
            return ResponseEntity.ok().body("EUI");
        }
        String result = userService.checkId(userId);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/check/nick")
    public ResponseEntity<String> checkNick(
            @RequestParam String userNick
    ){
        if(userNick.contains(" ")) {
            return ResponseEntity.ok().body("EW"); // 공백이 포함된 경우의 응답 코드 예
        }

        if(userNick.isEmpty()){
            return ResponseEntity.ok().body("EUN");
        }
        String result = userService.checkNick(userNick);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/send/email")
    public ResponseEntity<Map<String, Object>> sendEmail(
            @RequestParam String userEmail
    ){
        String result = userService.sendEmail(userEmail);
        long expiryTime = System.currentTimeMillis() + 180 * 1000;

        Map<String, Object> response = new HashMap<>();
        response.put("expiryTime", expiryTime);
        response.put("result", result);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/check/pwd")
    public ResponseEntity<String> checkPwd(
            @RequestBody PutUserReqDto request
    ){
        String result = userService.checkPwd(request.getPwd());


        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/pwd")
    public ResponseEntity<String> updatePwd(
            @RequestBody PutUserReqDto request
    ){
        String result = userService.updateUserPwd(request.getPwd());

        return ResponseEntity.ok().body(result);
    }

}
