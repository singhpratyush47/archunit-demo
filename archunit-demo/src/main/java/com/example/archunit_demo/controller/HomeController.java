package com.example.archunit_demo.controller;

import com.example.archunit_demo.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/home")
public class HomeController {

    @Autowired
    private HomeService homeService;
    @GetMapping("/welcome")
    public String getWelcomeMessage(){
        return homeService.getWelcomeMessage();
    }
}
