package com.loyayz.sample;

import com.loyayz.simple.annotation.ModelScan;
import com.loyayz.simple.annotation.ModelScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@SpringBootApplication
// 可单个
//@ModelScan
// 可多个
@ModelScans({
        @ModelScan("com.loyayz.sample")
})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
