package com.msa4meerkatgram.domain.user.responses;

import lombok.Builder;

@Builder
public record UserRes(
        long id
        ,String email
        ,String nick
        ,String role
        ,String profile
        ,String createdAt
        ,long countPosts
) {
}


/*
*
* 레코드는
* @Builder 가 있어야 하나봄
*
*
* */

