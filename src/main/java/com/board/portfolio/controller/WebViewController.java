package com.board.portfolio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebViewController {
    @GetMapping("/")
    public String indexView(){
        return "index";
    }
}
