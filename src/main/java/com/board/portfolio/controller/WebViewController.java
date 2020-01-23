package com.board.portfolio.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Locale;

@Controller
@RequiredArgsConstructor
public class WebViewController {

    @GetMapping("/")
    public ModelAndView indexView(Locale locale,
                                  ModelAndView mv){
        mv.addObject("locale", locale.toLanguageTag());
        mv.setViewName("index");
        return mv;
    }
}
