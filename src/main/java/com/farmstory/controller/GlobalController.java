package com.farmstory.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalController {

    @Value("${spring.application.version}")
    private String appVersion;


    @ModelAttribute
    public void addAttributes(HttpServletRequest request, Model model) {
        // 필터에서 설정한 속성 가져오기
        String dbToday = (String) request.getAttribute("dbToday");

        model.addAttribute("dbToday", dbToday);
        model.addAttribute("appVersion", appVersion);
    }
}
