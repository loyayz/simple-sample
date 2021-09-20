package io.simpleframework.sample;

import io.simpleframework.crud.annotation.ModelScan;
import io.simpleframework.crud.annotation.ModelScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@SpringBootApplication
// 可多个
@ModelScans({
        @ModelScan("io.simpleframework.sample.model")
})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
