package com.mindhub.homebanking.controllers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ExtrasController {
    @GetMapping("/report")
    public void convertPdf(){
        System.out.println("HOLA");
    }
}
