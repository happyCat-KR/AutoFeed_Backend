package kr.soft.autofeed.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class Main {
    
    @GetMapping("/")
    public String home() {
        return "index";
    }
    
    
}
