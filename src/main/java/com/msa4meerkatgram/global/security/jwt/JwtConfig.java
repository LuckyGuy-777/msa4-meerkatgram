package com.msa4meerkatgram.global.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.jwt")
public record JwtConfig(
        boolean secure,
        String issuer,
        String type,
        int accessTokenExpiry,
        int refreshTokenExpiry,
        String refreshTokenCookieName,
        int refreshTokenCookieExpiry,
        String secret,
        String headerKey,
        String scheme,
        String reissUri
) {

}

/*
* @ConfigurationProperties(prefix = "security.jwt")
*
* @ConfigurationProperties 은, 설정파일의 값을 자바클래스의
* 필드 변수에 자동으로 넣어주겠다 라는 의미
*
* prefix = "security.jwt" 는, 설정파일에 있는 값들 중,
* security.jwt 로 시작하는 설정값들만 모아서 이 클래스에 연결
*
* */