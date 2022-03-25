package com.tmobile.hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@EnableAutoConfiguration
@SpringBootApplication
public class SpringBootHelloWorld {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(SpringBootHelloWorld.class, args);
    }
}