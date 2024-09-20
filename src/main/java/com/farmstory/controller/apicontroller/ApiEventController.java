package com.farmstory.controller.apicontroller;

import com.farmstory.requestdto.user.DeleteScheduleReqDto;
import com.farmstory.requestdto.user.PostAdminScheduleReqDto;
import com.farmstory.requestdto.user.PostScheduleReqDto;
import com.farmstory.service.user.UserScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/event")
@RequiredArgsConstructor
public class ApiEventController {

    private final UserScheduleService userScheduleService;

    @PostMapping("/schedule")
    public ResponseEntity<String> postSchedule(
            @RequestBody PostScheduleReqDto dto
            ) {

        String result = userScheduleService.insertSchedule(dto);

        if("SU".equals(result)){
            return ResponseEntity.ok().body("http://localhost:8080/client/event?section=event&&type=schedule");
        }
        return ResponseEntity.ok().body("http://localhost:8080/view/login");
    }  // 일정 추가하기

    @DeleteMapping("/schedule")
    public ResponseEntity<String> deleteSchedule(
            @ModelAttribute DeleteScheduleReqDto request
    ){
        String result = userScheduleService.deleteSchedule(request);


        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/admin")
    public ResponseEntity<String> postAdmin(
            @RequestBody PostAdminScheduleReqDto request
            ){

        System.out.println(request);
        String result = userScheduleService.insertAdminSchedule(request);

        return ResponseEntity.ok().body("/client/event?section=event&type=schedule");
    }

    @DeleteMapping("admin")
    public ResponseEntity<String> deleteAdmin(
            @ModelAttribute DeleteScheduleReqDto request
    ){
        String result = userScheduleService.deleteAdminSchedule(request);


        return ResponseEntity.ok().body(result);
    }

}
