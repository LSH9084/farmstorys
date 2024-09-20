package com.farmstory.controller.pagecontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/view")
public class ViewController {

    @GetMapping("/signup")
    public ModelAndView signupPage(){
        ModelAndView mav = new ModelAndView();
//        mav.addObject("postUserRequestDto", new TestUserRequestDto());
        mav.setViewName("pages/auth/signup");
        return mav;
    }

    @GetMapping("/login")
    public ModelAndView loginPage(
            @RequestParam(value="error", defaultValue="false") String error
    ){
        ModelAndView mav = new ModelAndView();

        mav.setViewName("pages/auth/login");
        if(error.equals("true")){
            mav.addObject("error","error");
        }
        return mav;
    }

    @GetMapping("/find/id")
    public ModelAndView findId(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/pages/auth/find_id");

        return mav;
    }

    @GetMapping("/find/pwd")
    public ModelAndView findPwd(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/pages/auth/find_pwd");

        return mav;
    }



}
