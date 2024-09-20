package com.farmstory.controller.pagecontroller;

import com.farmstory.responsedto.cart.GetCartsRespDto;
import com.farmstory.responsedto.order.GetOrdersRespDto;
import com.farmstory.responsedto.user.GetMypageUserPointRespDto;
import com.farmstory.responsedto.user.GetUserAllInfoDto;
import com.farmstory.service.cart.CartService;
import com.farmstory.service.order.OrderService;
import com.farmstory.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MypageController {

    private final CartService cartService;
    private final OrderService orderService;
    private final UserService userService;

    @GetMapping("/carts")
    public ModelAndView getCarts(
            @RequestParam String section,
            @RequestParam String type
    ) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("pages/cart/cart_list");
        mav.addObject("section" ,section);
        mav.addObject("type", type);

        List<GetCartsRespDto> carts = cartService.selectCart();

        if(carts==null || carts.size()==0){
            mav.addObject("nullItem","nullItem");
        } else {
            mav.addObject("carts", carts);
        }

        return mav;
    }

    @GetMapping("/orders")
    public ModelAndView getOrders(
            @RequestParam String section,
            @RequestParam String type,
            @RequestParam(value="page", defaultValue="0") int page
    ) {
        Page<GetOrdersRespDto> orders = orderService.selectOrders(page);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("pages/order/order_list");
        mav.addObject("section" ,section);
        mav.addObject("type", type);
        mav.addObject("orders", orders);
        mav.addObject("currentPage", page);
        mav.addObject("totalPage", Math.ceil((double)orders.getTotalElements()/8));
        return mav;
    }

    @GetMapping("/user")
    public ModelAndView getUser(
            @RequestParam String section,
            @RequestParam String type
    ) {

        GetUserAllInfoDto user = userService.selectMypageUser();


        ModelAndView mav = new ModelAndView();
        mav.setViewName("pages/user/user_info");
        mav.addObject("section" ,section);
        mav.addObject("type", type);
        mav.addObject("user",user);
        return mav;
    }

    @GetMapping("/order")
    public ModelAndView getOrder(
            @RequestParam long orderIdx
    ){
        ModelAndView mav = new ModelAndView();
        List<GetOrdersRespDto> orders = orderService.getOrder(orderIdx);
        mav.addObject("orders", orders);
        mav.addObject("section" ,"mypage");
        mav.addObject("type", "order");
        mav.setViewName("pages/order/order_detail");

        return mav;
    }

    @GetMapping("/point")
    public ModelAndView getPoint(
            @RequestParam String section,
            @RequestParam String type,
            @RequestParam(value="page", defaultValue="0") int page
    ){
        ModelAndView mav = new ModelAndView();

        Page<GetMypageUserPointRespDto> points = userService.selectMypageUserPoint(page);

        BigDecimal totalPoint = userService.selectPoint();

        mav.setViewName("pages/user/user_point");
        mav.addObject("points",points);
        mav.addObject("totalPoint",totalPoint);
        mav.addObject("section" ,section);
        mav.addObject("type", type);

        return mav;
    }
    @GetMapping("/point/all")
    public ModelAndView getPointall(
            @RequestParam String section,
            @RequestParam String type
    ){
        ModelAndView mav = new ModelAndView();
        List<GetMypageUserPointRespDto> points = userService.selectMypageUserAll();

        BigDecimal totalPoint = userService.selectPoint();
        mav.setViewName("pages/user/user_point_all");
        mav.addObject("points",points);
        mav.addObject("totalPoint",totalPoint);
        mav.addObject("section" ,section);
        mav.addObject("type", type);

        return mav;
    }


}
