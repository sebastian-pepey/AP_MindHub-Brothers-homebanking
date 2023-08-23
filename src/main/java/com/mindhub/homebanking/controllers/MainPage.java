package com.mindhub.homebanking.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainPage {
    @RequestMapping(value = "/")
    public String redirect(){
        return "redirect:/web/index.html";
    }
}
