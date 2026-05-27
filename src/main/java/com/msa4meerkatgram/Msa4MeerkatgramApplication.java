package com.msa4meerkatgram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan // JWT 관련 어노테이션
public class Msa4MeerkatgramApplication {

    public static void main(String[] args) {
        SpringApplication.run(Msa4MeerkatgramApplication.class, args);
    }

}
