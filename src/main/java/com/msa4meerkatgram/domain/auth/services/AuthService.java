package com.msa4meerkatgram.domain.auth.services;

import com.msa4meerkatgram.domain.auth.mapper.AuthMapper;
import com.msa4meerkatgram.domain.auth.requests.LoginReq;
import com.msa4meerkatgram.domain.auth.responses.AuthRes;
import com.msa4meerkatgram.domain.user.entities.User;
import com.msa4meerkatgram.domain.user.mapper.UserMapper;
import com.msa4meerkatgram.domain.user.responses.UserRes;
import com.msa4meerkatgram.global.errors.custom.NotRegisteredException;
import com.msa4meerkatgram.global.security.cookie.CookieManager;
import com.msa4meerkatgram.global.security.jwt.JwtConfig;
import com.msa4meerkatgram.global.security.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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