package com.farmstory.controller.apicontroller;

import com.farmstory.responsedto.order.GetOrdersRespDto;
import com.farmstory.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class ApiMypageController {

    private final OrderService orderService;

    @GetMapping("/order")
    public ResponseEntity<Map<String,Object>> getOrder(
            @RequestParam long orderIdx
    ){
        Map<String,Object> map = new HashMap<>();
        List<GetOrdersRespDto> orders = orderService.getOrder(orderIdx);
        map.put("orders", orders);

        System.out.println(orders);

        return ResponseEntity.ok().body(map);
    }
}
