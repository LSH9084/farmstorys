package com.farmstory.controller.pagecontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/client")
public class CartController {

    @GetMapping("/carts")
    public ModelAndView getCart() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("pages/cart/cart_list");
        mav.addObject("section" ,"product");
        return mav;
    }
}
