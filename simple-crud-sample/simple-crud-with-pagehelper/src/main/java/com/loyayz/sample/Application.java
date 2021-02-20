package com.loyayz.sample;

import com.loyayz.simple.annotation.ModelScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@SpringBootApplication
@ModelScan
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
