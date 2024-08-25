package com.shinhan.dongibuyeo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DongibuyeoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DongibuyeoApplication.class, args);
    }

}
