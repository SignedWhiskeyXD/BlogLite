package top.whiskeyxd.blogliteauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"top.whiskeyxd"})
public class BlogLiteAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogLiteAuthApplication.class, args);
    }

}
