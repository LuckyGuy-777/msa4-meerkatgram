package com.msa4meerkatgram.domain.user.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserMybatis {
    private long id;
    private String email;
    private String password;
    private String nick;
    private String provider;
    private String role;
    private String profile;
    private String refreshToken;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;
}

// 엔티티 :  데이터베이스 하나의 레코드 정보를 담는 객체
