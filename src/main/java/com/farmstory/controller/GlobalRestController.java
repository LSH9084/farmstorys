package com.farmstory.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalRestController {

    @ModelAttribute
    public void addAttributes(HttpServletRequest request, Model model) {
        // 필터에서 설정한 속성 가져오기
        String dbToday = (String) request.getAttribute("dbToday");

        model.addAttribute("dbToday", dbToday);
    }
}
