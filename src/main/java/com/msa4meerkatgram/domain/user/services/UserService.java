package com.msa4meerkatgram.domain.user.services;

import com.msa4meerkatgram.global.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtProvider jwtProvider;


}

// @RequiredArgsConstructor 외부에서 주입받은 객체 사용하려면 필요하다함
