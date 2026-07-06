package com.msa4meerkatgram.domain.auth.mapper;

import com.msa4meerkatgram.domain.user.entities.UserMybatis;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthMapper {

    int updateRefreshToken(long id,String refreshToken);
    int create(UserMybatis user);

}


/*
*  int updateRefreshToken(long id,String refreshToken);
*  유저아이디와, 리프레시 토큰을, 인수로 받는 추상메소드 생성
*
*
*
*
*
*
* -------------------
*  메퍼 만드는 방법
*
*  1. 어노테이션 @Mapper
*
*  2. 추상메소드 생성
*
* */
