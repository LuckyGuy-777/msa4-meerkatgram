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
        responseCode = "401"
        ,description = "로그인이 필요한 서비스 입니다"
        ,content = @Content(
                mediaType = "application/json"
                ,examples = {
                @ExampleObject(
                        name = "로그인이 필요한 서비스 입니다"
                        ,value = "{\"code\":\"E02\",\"message\":\"UNAUTHENTICATED_ERROR\"}"
                )
        }
    )
)
public @interface ApiUnauthenticatedErrorResponse {

}
