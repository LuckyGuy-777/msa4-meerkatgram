package com.msa4meerkatgram.domain.auth.services;

import com.msa4meerkatgram.domain.auth.repositories.AuthRepository;
import com.msa4meerkatgram.domain.auth.requests.LoginReq;
import com.msa4meerkatgram.domain.auth.requests.RegistrationReq;
import com.msa4meerkatgram.domain.auth.responses.AuthRes;
import com.msa4meerkatgram.domain.post.repositories.PostRepository;
import com.msa4meerkatgram.domain.user.entities.User;
import com.msa4meerkatgram.global.errors.custom.DuplicatedRecordException;
import com.msa4meerkatgram.global.errors.custom.InvalidTokenException;
import com.msa4meerkatgram.global.errors.custom.NotRegisteredException;
import com.msa4meerkatgram.global.security.constant.ProviderPolicy;
import com.msa4meerkatgram.global.security.constant.RolePolicy;
import com.msa4meerkatgram.global.security.cookie.CookieManager;
import com.msa4meerkatgram.global.security.jwt.JwtConfig;
import com.msa4meerkatgram.global.security.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {


    private final JwtProvider jwtProvider;
    private final CookieManager cookieManager;
    private final JwtConfig jwtConfig;
    private final PasswordEncoder passwordEncoder;
    private final AuthRepository authRepository;
    private final PostRepository postRepository;

    public AuthRes login(HttpServletResponse response, LoginReq loginReq){
        // 유저정보 획득 + 유저 가입 여부 확인
        User user = authRepository.findByEmail(loginReq.email())
                .orElseThrow(() -> new NotRegisteredException("아이디와 비밀번호를 확인해 주세요."));



        // 비밀번호 체크
        if(!passwordEncoder.matches(loginReq.password(), user.getPassword())) {
            throw new NotRegisteredException("아이디와 비밀번호를 확인해주세요.");  // 보안때문에, 아이디 예외문구와 같은 문구.
        }

        return this.generateAuthentication(response,user);
    }

    // 토큰 재 생성 로직
    public AuthRes reissue(HttpServletRequest request, HttpServletResponse response) {
        // 리프레시 토큰 획득
//        Optional<String> refreshTokenOptional = jwtProvider.extractRefreshToken(request);
//        if(refreshTokenOptional.isEmpty()){
//            throw new InvalidTokenException("토큰이 없습니다.");
//        }
//
        String extractRefreshToken = jwtProvider.extractRefreshToken(request)
                .orElseThrow(()-> new InvalidTokenException("토큰이 없습니다."));


        long id = Long.parseLong(jwtProvider.extractClaim(extractRefreshToken).getSubject());

        // 유저 획득 및 유저 가입 여부 확인
        User user = authRepository.findById(id)
                .orElseThrow(() -> new InvalidTokenException("유효하지 않은 회원의 토큰입니다."));

        // 비로그인 상태 확인
        if(user.getRefreshToken() == null) {
            throw new InvalidTokenException("유효하지 않은 회원의 토큰입니다.");
        }

        // 리프레시 토큰 비교
        if(!user.getRefreshToken().equals(extractRefreshToken)){
            throw new InvalidTokenException("토큰이 일치하지 않습니다.");
        }
        return this.generateAuthentication(response,user);
    }

    private AuthRes generateAuthentication(HttpServletResponse response, User user) {

        // 작성 게시글 수 획득
        long countPosts = postRepository.countByUser(user);

        // 토큰생성
        String newAccessToken = jwtProvider.generateAccessToken(user);
        String newRefreshToken = jwtProvider.generateRefreshToken(user);


        // 리프레시 토큰을 db에 저장
        user.setRefreshToken(newRefreshToken);
        authRepository.save(user);

        // 리프레시 토큰을 쿠키에
        cookieManager.setCookie(
                response
                , jwtConfig.refreshTokenCookieName()
                , newRefreshToken
                , jwtConfig.refreshTokenCookieExpiry()
                , jwtConfig.reissUri()
        );


        // 리턴
        return AuthRes.from(user,newAccessToken,countPosts);
    }

    // @Transactional(rollbackFor = Exception.class) : 어떤 예외가 발생해도 롤백한다
    @Transactional(rollbackFor = Exception.class)
    public void logout(HttpServletResponse response, long id) {


        // 유저 정보 획득
        User user = authRepository.findById(id)
                .orElseThrow(() -> new InvalidTokenException("유효하지 않은회원의 토큰입니다"));


        // DB 에 저장한 리프레시 토큰 파기
        user.setRefreshToken(null);
        authRepository.save(user);

        // Cookie에 저장한 리프레시 토큰 파기
        cookieManager.setCookie(
                response
                ,jwtConfig.refreshTokenCookieName()
                ,null
                ,0
                ,jwtConfig.reissUri()
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public void registration(RegistrationReq registrationReq) {
        // 유저 정보
        if (authRepository.existsByEmail(registrationReq.email())) {

            throw  new DuplicatedRecordException("이미 가입된 회원입니다.");
        }



        User newUser = new User();
        newUser.setEmail(registrationReq.email());
        newUser.setPassword(passwordEncoder.encode(registrationReq.password()));
        newUser.setNick(registrationReq.nick());
        newUser.setProfile(registrationReq.profile());
        newUser.setProvider(ProviderPolicy.NONE);
        newUser.setRole(RolePolicy.NORMAL);
        authRepository.save(newUser);
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
*   ,jwtConfig.reissUri()
*    url 까지 같아야 같은 쿠키로 인식하고, 쿠키 초기화해줌
* */