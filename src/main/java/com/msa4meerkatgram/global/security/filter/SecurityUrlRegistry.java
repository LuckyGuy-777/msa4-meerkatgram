package com.msa4meerkatgram.global.security.filter;

// 스프링 시큐리티 PATH 목록 페이지.

// 블랙리스트, 화이트리스트 모두, 내 프로젝트에 맞게 설정해야함
// 각 요청별, 인증이 필요한 페이지를 설정해둠.

public final class SecurityUrlRegistry {
    private SecurityUrlRegistry() {} // 인스턴스 생성 방지

    // ----------------
    // 블랙리스트 (인증 반드시 필요).
    // ----------------
    public static final String[] AUTH_REQUIRED_GET_URLS = {
            "/api/posts/{id}"
    };
    public static final String[] AUTH_REQUIRED_POST_URLS = {
            "/api/logout"
            ,"/api/posts"
    };
    public static final String[] AUTH_REQUIRED_PUT_URLS = {

    };
    public static final String[] AUTH_REQUIRED_PATCH_URLS = {

    };
    public static final String[] AUTH_REQUIRED_DELETE_URLS = {
            "/api/posts/{id}"
    };


}
