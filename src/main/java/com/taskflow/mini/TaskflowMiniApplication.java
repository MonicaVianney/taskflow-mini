package com.taskflow.mini;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class
})
@ComponentScan(basePackages = "com.taskflow.mini")
public class TaskflowMiniApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskflowMiniApplication.class, args);
    }

}