package com.example.projectapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProjectApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectApiApplication.class, args);
    }

}
