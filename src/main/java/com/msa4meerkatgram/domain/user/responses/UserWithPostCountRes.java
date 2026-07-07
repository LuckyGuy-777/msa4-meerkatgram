package com.msa4meerkatgram.domain.user.responses;

import com.msa4meerkatgram.domain.user.entities.User;


public record UserWithPostCountRes(
        UserRes user // 필요한 정보를 저장하고, 다른레이어에 전달하기 위한 역할 (데이터만 기본적으로 저장)
        ,long countPosts // 필요한 서브정보 도 담아서 전달
) {
    public static UserWithPostCountRes from(User user, long countPosts){
        return new UserWithPostCountRes(
                UserRes.from(user) // 유저 response dto 를 만들고
                , countPosts
        );
    }
}


/*
*
* 레코드는
* @Builder 가 있어야 하나봄
*
*UserRes user // 필요한 정보를 저장하고, 다른레이어에 전달하기 위한 역할 (데이터만 기본적으로 저장)
    다른 레이어에 데이터를 전달하고 싶을때 ,이 dto 객체에 담아서, 이 dto 객체를 다른 서비스객체에 전달한다.
*
*
* countPosts -> 이 유저가 가진 게시글 수 를 저장하고있다.
*
* */

