package com.msa4meerkatgram.global.annotations.openapi;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)

@ApiResponse(
        description = "유효성 검사 실패"
        ,content = @Content(
        mediaType = "application/json"
        ,examples = {
        @ExampleObject(
                name = "유효성 검사 실패"
                    , value = "{\"code\":\"E21\",\"message\":\"Bad Request\"}"
            )
        }
    )
)
public @interface ApiNotValidErrorResponse {
}

// 어떤 레벨에 줄 어노테이션인지 알려주는것 :@Target(ElementType.METHOD) -> METHOD 는, 메소드레벨에 주는것
// @Retention(RetentionPolicy.RUNTIME) 은, 런 타임에 동작해야하는 어플리케이션 이라는뜻

//