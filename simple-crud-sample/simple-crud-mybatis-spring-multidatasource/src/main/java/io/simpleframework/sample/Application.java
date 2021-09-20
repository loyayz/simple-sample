package io.simpleframework.sample;

import io.simpleframework.crud.annotation.ModelScan;
import io.simpleframework.crud.annotation.ModelScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@SpringBootApplication
@ModelScans({
        @ModelScan(value = "io.simpleframework.sample.model", datasourceName = MybatisConfig.DS_ONE),
        @ModelScan(value = "io.simpleframework.sample.two", datasourceName = MybatisConfig.DS_TWO)
})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
