package com.msa4meerkatgram.domain.user.responses;

import com.msa4meerkatgram.domain.user.entities.User;
import com.msa4meerkatgram.global.security.constant.RolePolicy;

import java.time.LocalDateTime;


public record UserRes(
        long id
        , String email
        , String nick
        , RolePolicy role
        , String profile
        , LocalDateTime createdAt
) {
    public static UserRes from(User user){
        return new UserRes(
                user.getId()
                , user.getEmail()
                , user.getNick()
                , user.getRole()
                , user.getProfile()
                , user.getCreatedAt()
        );
    }
}


/*
*
* 레코드는
* @Builder 가 있어야 하나봄
*
*
*
*  엔티티 기반의 베이스 dto 를 만들고
*
*  필요한 정보가 더 있다면, dto를 더 만듬
*  베이스 *dto에, 추가1 , 추가2 등으로 형성됨
*
*  이러한 방식으로, 필요한 정보가 더 생긴다면
*  베이스 *dto에, 추가1 , 추가2 등으로 형성됨.
*
*
*  평등한 관계이고 필요할 때 참조해서 사용함( has - a ) 관계
*
* */

