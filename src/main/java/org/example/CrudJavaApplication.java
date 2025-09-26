package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("org.example.library")
@EnableJpaRepositories("org.example.library")
@EnableJpaAuditing
public class CrudJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrudJavaApplication.class, args);
    }
}