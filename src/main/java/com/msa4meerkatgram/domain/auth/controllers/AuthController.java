package com.msa4meerkatgram.domain.auth.controllers;


import com.msa4meerkatgram.domain.auth.requests.LoginReq;
import com.msa4meerkatgram.domain.auth.requests.RegistrationReq;
import com.msa4meerkatgram.domain.auth.responses.AuthRes;
import com.msa4meerkatgram.domain.auth.services.AuthService;
import com.msa4meerkatgram.global.config.openapi.CustomApiResponse;
import com.msa4meerkatgram.global.responses.GlobalRes;
import com.msa4meerkatgram.global.responses.constant.CustomResponseCode;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "인증 API", description = "인증 및 인가 담당 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "로그인처리", description = "이메일과 비밀번호로 로그인")
    @CustomApiResponse(value = {
            CustomResponseCode.INVALID_PARAMETER_ERROR,
            CustomResponseCode.NOT_REGISTERED_ERROR,
            CustomResponseCode.DB_ERROR,
            CustomResponseCode.SYSTEM_ERROR
    })
    @PostMapping("/login")
    public ResponseEntity<GlobalRes<AuthRes>> login(
            @Valid @RequestBody LoginReq loginReq
            , HttpServletResponse response
    ) {
        return ResponseEntity.ok(GlobalRes.success(authService.login(response,loginReq)));

    }

    // 새로운 인증정보 생성 로직
    @Operation(summary = "토큰 재발급 처리")
    @CustomApiResponse(value = {
            CustomResponseCode.INVALID_TOKEN_ERROR,
            CustomResponseCode.DB_ERROR,
            CustomResponseCode.SYSTEM_ERROR
    })
    @PostMapping("/reissue-token")
    public ResponseEntity<GlobalRes<AuthRes>> reissue(
            HttpServletRequest request
            , HttpServletResponse response
    ) {
        return ResponseEntity.ok(GlobalRes.success(authService.reissue(request,response)));

    }



    @Operation(summary = "로그아웃 처리")
    @CustomApiResponse(value = {
            CustomResponseCode.UNAUTHENTICATED_ERROR,
            CustomResponseCode.INVALID_TOKEN_ERROR,
            CustomResponseCode.DB_ERROR,
            CustomResponseCode.SYSTEM_ERROR
    })
    @PostMapping("/logout")
    public ResponseEntity<GlobalRes<Void>> logout(
        HttpServletResponse response
        ,@AuthenticationPrincipal Claims claims
    ) {
        authService.logout(response,Long.parseLong(claims.getSubject()));

        return ResponseEntity.ok(GlobalRes.success());
    }


    @Operation(summary = "회원가입 처리")
    @CustomApiResponse(value = {
            CustomResponseCode.INVALID_PARAMETER_ERROR,
            CustomResponseCode.DUPLICATED_RECORD_ERROR,
            CustomResponseCode.DB_ERROR,
            CustomResponseCode.SYSTEM_ERROR
    })
    @PostMapping("/registration")
    public ResponseEntity<GlobalRes<Void>> registration(
        @Valid @RequestBody RegistrationReq registrationReq
        ) {
        authService.registration(registrationReq);

        return ResponseEntity.ok(GlobalRes.success());
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