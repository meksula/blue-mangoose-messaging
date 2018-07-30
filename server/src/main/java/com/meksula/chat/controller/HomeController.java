package com.meksula.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @Author
 * Karol Meksuła
 * 20-07-2018
 * */

@Controller
public class HomeController {

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public String home() {
        return "home";
    }



}
