package io.simpleframework.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    public static final String LONG_ID_MODEL_NAME = "long_id_model";
    public static final String STRING_ID_MODEL_NAME = "string_id_model";
    public static final String AUTO_ID_MODEL_NAME = "auto_id_model";

}
