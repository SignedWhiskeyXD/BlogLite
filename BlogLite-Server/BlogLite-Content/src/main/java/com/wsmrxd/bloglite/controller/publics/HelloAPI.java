package com.wsmrxd.bloglite.controller.publics;

import com.wsmrxd.bloglite.vo.RestResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloAPI {

    @RequestMapping("/hello")
    public RestResponse<String> sayHello(){
        return RestResponse.ok("Hello from Spring Boot!");
    }
}
