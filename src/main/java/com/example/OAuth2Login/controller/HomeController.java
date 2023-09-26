package com.example.OAuth2Login.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class HomeController {

    @GetMapping({"/","/home"})
    String home(){
        return "hi, how is it going??";
    }

}
