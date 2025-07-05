package com.beebee.caronas.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teste")
@CrossOrigin(origins= "4200")
public class TesteController {
    @GetMapping
    public String teste() {
        return "API funcionando";
    }
}
