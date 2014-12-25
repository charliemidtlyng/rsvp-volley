package com.backbase.progfun;

import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import static org.slf4j.LoggerFactory.getLogger;

@EnableAutoConfiguration
@ComponentScan
public class Application {

    private static final Logger LOGGER = getLogger(Application.class);

    public static void main(String[] args) throws Throwable {
        SpringApplication.run(Application.class, args);
    }

}