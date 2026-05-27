package com.msa4meerkatgram.domain.auth.controllers;


import com.msa4meerkatgram.domain.auth.requests.LoginReq;
import com.msa4meerkatgram.domain.auth.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @Valid @RequestBody LoginReq loginReq
            , HttpServletResponse response
    ) {
        authService.login(loginReq);

        return ResponseEntity.status(200).body("test");
    }
}



// @Valid @RequestBody LoginReq loginReq  는, 로그인 요청의 정보를 받아올 객체
// 해당 dto 객체에서 , 유효성 검사하도록 @Valid  를 줌
//  HttpServletResponse response  는  서버가 클라이언트할때 response 할때 필요한 정보를 담는 객체 , 쿠키 저장할 객체,