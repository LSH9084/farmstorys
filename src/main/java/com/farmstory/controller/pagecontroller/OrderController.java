package com.farmstory.controller.pagecontroller;

import com.farmstory.responsedto.order.GetOrderDirectRespDto;
import com.farmstory.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/client")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/order/direct")
    public ModelAndView postOrder(
            @RequestParam Long prodIdx,
            @RequestParam int quantity
            ){
        ModelAndView mav = new ModelAndView();

        GetOrderDirectRespDto orderDirect = orderService.selectOrderDirect(prodIdx,quantity);

        mav.setViewName("/pages/order/order_direct");
        mav.addObject("orderDirect", orderDirect);
        mav.addObject("section","product");

        return mav;
    }
}
