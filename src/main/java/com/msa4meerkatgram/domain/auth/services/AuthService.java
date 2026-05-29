package com.msa4meerkatgram.domain.auth.services;

import com.msa4meerkatgram.domain.auth.mapper.AuthMapper;
import com.msa4meerkatgram.domain.auth.requests.LoginReq;
import com.msa4meerkatgram.domain.auth.responses.AuthRes;
import com.msa4meerkatgram.domain.user.entities.User;
import com.msa4meerkatgram.domain.user.mapper.UserMapper;
import com.msa4meerkatgram.domain.user.responses.UserRes;
import com.msa4meerkatgram.global.errors.custom.InvalidTokenException;
import com.msa4meerkatgram.global.errors.custom.NotRegisteredException;
import com.msa4meerkatgram.global.security.cookie.CookieManager;
import com.msa4meerkatgram.global.security.jwt.JwtConfig;
import com.msa4meerkatgram.global.security.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserMapper userMapper;
    private final JwtProvider jwtProvider;
    private final AuthMapper authMapper;
    private final CookieManager cookieManager;
    private final JwtConfig jwtConfig;

    public AuthRes login(HttpServletResponse response, LoginReq loginReq){
        // 유저정보 획득
        User user = userMapper.findByEmail(loginReq.email());


        // 유저 가입 여부 확인
        if(user == null) {
            throw new NotRegisteredException("아이디와 비밀번호를 확인해주세요.");
        }


        // 비밀번호 체크

        return this.generateAuthentication(response,user);
    }

    // 토큰 재 생성 로직
    public AuthRes reissue(HttpServletRequest request, HttpServletResponse response) {
        // 리프레시 토큰 획득
        Optional<String> refreshTokenOptional = jwtProvider.extractRefreshToken(request);
        if(refreshTokenOptional.isEmpty()){
            throw new InvalidTokenException("토큰이 없습니다.");
        }
        String extractRefreshToken = refreshTokenOptional.get();

        long id = Long.parseLong(jwtProvider.extractClaim(extractRefreshToken).getSubject());

        // 유저 획득
        User user = userMapper.findByPk(id);

        // 유저 가입 여부 확인
        if(user == null) {
            throw new InvalidTokenException("유효하지 않은 회원의 토큰입니다.");
        }

        // 리프레시 토큰 비교
        if(!user.getRefreshToken().equals(extractRefreshToken)){
            throw new InvalidTokenException("토큰이 일치하지 않습니다.");
        }
        return this.generateAuthentication(response,user);
    }

    private AuthRes generateAuthentication(HttpServletResponse response, User user) {
        // 토큰생성
        String newAccessToken = jwtProvider.generateAccessToken(user);
        String newRefreshToken = jwtProvider.generateRefreshToken(user);


        // 리프레시 토큰을 db에 저장
        authMapper.updateRefreshToken(user.getId(), newRefreshToken);

        // 리프레시 토큰을 쿠키에
        cookieManager.setCookie(
                response
                , jwtConfig.refreshTokenCookieName()
                , newRefreshToken
                , jwtConfig.refreshTokenCookieExpiry()
                , jwtConfig.reissUri()
        );


        // 리턴
        return AuthRes.builder()
                .accessToken(newAccessToken)
                .user(
                        UserRes.builder()
                                .email(user.getEmail())
                                .nick(user.getNick())
                                .role(user.getRole())
                                .profile(user.getProfile())
                                .createdAt(user.getCreatedAt())
                                .build()
                ).build();
    }

}


/*
*  String newAccessToken = jwtProvider.generateAccessToken(user);
*   엑세스토큰을, 유저정보를 받아서 생성
*
   String newRefreshToken = jwtProvider.generateRefreshToken(user);
*   리프레시토큰을, 유저정보를 받아서 생성
*
*  authMapper.updateRefreshToken(user.getId(), newRefreshToken)
*  authMapper의 추상메소드 updateRefreshToken를 사용해서
*  DB에 접근. 유저메퍼에서, 아이디를 가져온 값과, 리프레시토큰을 인수로 주고,
*  db 에 업데이트 시킴
*
* */