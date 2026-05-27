package com.msa4meerkatgram.global.security.jwt;

import com.msa4meerkatgram.domain.user.entities.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

// 토큰을 생성해서 주입해주는 역할을함.

// 자동으로 자바 빈 으로 등록해주것(자동으로 자바가 메모리에 올려주는 객체)
// 클래스레벨 에서 사용됨 @Component
@Component
public class JwtProvider {
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    // 생성자
    public JwtProvider(JwtConfig jwtConfig){
        this.jwtConfig = jwtConfig;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConfig.secret()));
    }

    // 토큰생성
    private String generateToken(User user, long ttl) {
        Date now = new Date();

        return Jwts.builder()
                .header()
                .type(jwtConfig.type())
                .and()
                .subject(String.valueOf(user.getId()))
                .issuer(jwtConfig.issuer())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + ttl))
                .claim("role", user.getRole())
                .signWith(secretKey) // 시그니처
                .compact();
    }


    public String generateAccessToken(User user) {
        // 제너레이트 토큰 호출 후, 반환하는 기능
        return this.generateToken(user, jwtConfig.accessTokenExpiry());
    }

    public String generateRefreshToken(User user) {
        // 리프레시 토큰 호출 후, 반환하는 기능
        return this.generateToken(user, jwtConfig.refreshTokenExpiry());
    }


}

// .header() 헤더 세팅
// .type(jwtConfig.type()) 토큰유형 설정
// .and() , 추가연결 하는기능. 부터 페이로드 부분이라고 함
// .subject() 는 유저값을 받는부분. 유저를 특정하는 id세팅에 주로 사용
//  .issuer(jwtConfig.issuer()) 는 토큰 발급자
// .issuedAt( ) 는 토큰 발급일자
// .expiration() 는, 토큰 만료시간
// .claim("role", user.getRole()) 는 private claim 설정
// .signWith(secretKey)  시그니처 세팅