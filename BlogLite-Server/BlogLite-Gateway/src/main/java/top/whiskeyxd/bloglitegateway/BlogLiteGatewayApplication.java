package top.whiskeyxd.bloglitegateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"top.whiskeyxd"})
public class BlogLiteGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogLiteGatewayApplication.class, args);
    }

}
