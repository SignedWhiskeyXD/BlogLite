package com.wsmrxd.bloglite.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {

    @RequestMapping("/hello")
    public String sayHello(){
        return "Hello from Spring Boot!";
    }
}
