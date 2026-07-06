package com.msa4meerkatgram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@ConfigurationPropertiesScan // JWT 관련 어노테이션
@EnableJpaAuditing // 스프링부트 어플리케이션 전체에, JPA Auditing 기능을 사용할테니 준비 하라 선언.
public class Msa4MeerkatgramApplication {

    public static void main(String[] args) {
        SpringApplication.run(Msa4MeerkatgramApplication.class, args);
    }

}
