package com.wsmrxd.bloglite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BlogLiteContentApp {

    public static void main(String[] args) {
        SpringApplication.run(BlogLiteContentApp.class, args);
    }

}
