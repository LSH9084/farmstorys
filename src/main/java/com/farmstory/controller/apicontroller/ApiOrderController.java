package com.farmstory.controller.apicontroller;

import com.farmstory.requestdto.order.PostOrderDirectReqDto;
import com.farmstory.requestdto.order.PostOrderReqDto;
import com.farmstory.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ApiOrderController {

    private final OrderService orderService;

    @PostMapping("/order/direct")
    public ResponseEntity<String> postOrderDirect(
            @RequestBody PostOrderDirectReqDto request
    ){
        orderService.insertOrderDirect(request);
        String path = "mypage/orders?section=mypage&type=order";
        return ResponseEntity.ok().body(path);
    }

    @PostMapping("/order")
    public ResponseEntity<String> postOrder(
            @RequestBody PostOrderReqDto request
    ){
        System.out.println(request);
        String result = orderService.insertOrder(request);

        String path = "mypage/orders?section=mypage&type=order";

        return ResponseEntity.ok().body(path);
    }

}
