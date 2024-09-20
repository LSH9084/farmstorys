package com.farmstory.controller.pagecontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IntroController {

    @GetMapping("/sidebar")
    public ModelAndView introGreeting(@RequestParam String section, String type) {
        if(section.equals("intro")){
            ModelAndView mav = new ModelAndView();
            mav.setViewName("pages/intro");
            mav.addObject("section", section);
            mav.addObject("type", type);
            return mav;
        }

        return null;
    }
}