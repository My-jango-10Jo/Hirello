package com.sparta.hirello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HirelloApplication {

    public static void main(String[] args) {
        SpringApplication.run(HirelloApplication.class, args);
    }

}
