package com.msa4meerkatgram.domain.auth.controllers;


import com.msa4meerkatgram.domain.auth.requests.LoginReq;
import com.msa4meerkatgram.domain.auth.requests.RegistrationReq;
import com.msa4meerkatgram.domain.auth.responses.AuthRes;
import com.msa4meerkatgram.domain.auth.services.AuthService;
import com.msa4meerkatgram.global.response.GlobalRes;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {
    private final AuthService authService;
//
    @PostMapping("/login")
    public ResponseEntity<GlobalRes<AuthRes>> login(
            @Valid @RequestBody LoginReq loginReq
            , HttpServletResponse response
    ) {
        return ResponseEntity.status(200).body(
                GlobalRes.<AuthRes>builder()
                        .code("00")
                        .message("로그인 완료")
                        .data(authService.login(response,loginReq))
                        .build()
        );
    }

    // 새로운 인증정보 생성 로직
    @PostMapping("/reissue-token")
    public ResponseEntity<GlobalRes<AuthRes>> reissue(
            HttpServletRequest request
            , HttpServletResponse response
    ) {
        return ResponseEntity.status(200).body(
                GlobalRes.<AuthRes>builder()
                        .code("00")
                        .message("토큰 재발급 완료")
                        .data(authService.reissue(request,response))
                        .build()
        );
    }



    @PostMapping("/logout")
    public ResponseEntity<GlobalRes<String>> logout(
        HttpServletResponse response
        ,@AuthenticationPrincipal Claims claims
    ) {
        authService.logout(response,Long.parseLong(claims.getSubject()));

        return ResponseEntity.status(200).body(
                GlobalRes.<String>builder()
                        .code("00")
                        .message("로그아웃 완료")
                        .build()
        );
    }


    @PostMapping("/registration")
    public ResponseEntity<GlobalRes<String>> registration(
        @Valid @RequestBody RegistrationReq registrationReq
        ) {
        authService.registration(registrationReq);

        return ResponseEntity.status(200).body(
                GlobalRes.<String>builder()
                        .code("00")
                        .message("회원가입 완료")
                        .build()
        );
    }


    // @AuthenticationPrincipal  는,
    // Spring Security에서 로그인한 사용자 정보를 컨트롤러(Controller)의
    // 파라미터로 직접 주입받을 수 있게 해주는 어노테이션


}



// @Valid @RequestBody LoginReq loginReq  는, 로그인 요청의 정보를 받아올 객체
// 해당 dto 객체에서 , 유효성 검사하도록 @Valid  를 줌
//  HttpServletResponse response  는  서버가 클라이언트할때 response 할때 필요한 정보를 담는 객체 , 쿠키 저장할 객체,

/*
*
* 없던데이터를 만들어서 다시 response 해주는것도
* postmapping 이라고 봐도된다
*
*HttpServletRequest request : 아이피번호, 언제들어왔냐, 등등이 들어있다고함
* HttpServletResponse response : 새로운 리프레시토큰 등.. 유저에게 반환될 값이 담김
* */