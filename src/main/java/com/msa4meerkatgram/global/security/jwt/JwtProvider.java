package com.msa4meerkatgram.global.security.jwt;

import com.msa4meerkatgram.domain.user.entities.UserMybatis;
import com.msa4meerkatgram.global.errors.custom.InvalidTokenException;
import com.msa4meerkatgram.global.security.cookie.CookieManager;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

// 토큰을 생성해서 주입해주는 역할을함.

// 자동으로 자바 빈 으로 등록해주것(자동으로 자바가 메모리에 올려주는 객체)
// 클래스레벨 에서 사용됨 @Component
@Component
public class JwtProvider {
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;
    private final CookieManager cookieManager;

    // 생성자
    public JwtProvider(JwtConfig jwtConfig, CookieManager cookieManager){
        this.jwtConfig = jwtConfig;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConfig.secret()));
        this.cookieManager = cookieManager;
    }

    // 토큰생성
    private String generateToken(UserMybatis user, long ttl) {
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


    public String generateAccessToken(UserMybatis user) {
        // 제너레이트 토큰 호출 후, 반환하는 기능
        return this.generateToken(user, jwtConfig.accessTokenExpiry());
    }

    public String generateRefreshToken(UserMybatis user) {
        // 리프레시 토큰 호출 후, 반환하는 기능
        return this.generateToken(user, jwtConfig.refreshTokenExpiry());
    }


    // 쿠키에서 리프레시 토큰 추출함
    public Optional<String> extractRefreshToken(HttpServletRequest request){
        return  cookieManager.getCookie(request, jwtConfig.refreshTokenCookieName())
                .map(Cookie::getValue);
    }

    /*
    *
    *
    *
    * '*/

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(jwtConfig.headerKey());

        if(bearerToken == null || !bearerToken.startsWith(jwtConfig.scheme())) {
            return Optional.empty();
        }

        return Optional.of(bearerToken.substring(jwtConfig.scheme().length()).trim());
    }

    // 토큰 검증 및 클레임추출
    public Claims extractClaim(String token){
        try{
            return  Jwts.parser()
                    .verifyWith(this.secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new InvalidTokenException("토큰이 만료 됬습니다.");
        } catch (UnsupportedJwtException e) {
            throw new InvalidTokenException("서명이 위조된 토큰 입니다,");
        } catch (MalformedJwtException e) {
            throw new InvalidTokenException("토큰 형식이 올바르지 않습니다.");
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException("토큰 검증에 실패 했습니다");
        }









































































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