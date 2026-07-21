package com.msa4meerkatgram.domain.post.responses;

import com.msa4meerkatgram.domain.post.entities.Post;
import lombok.Builder;

import java.util.List;

@Builder
public record PostIndexRes(
        long total
        , boolean lastPage
        , List<PostWithUserRes> posts
        ) {

        public static PostIndexRes from(long total, boolean lastPage, List<Post> posts) {

                return new PostIndexRes(
                        total
                        ,lastPage
                        ,posts.stream().map(PostWithUserRes::from).toList() // <- 메소드 레퍼런스

                );
        }
}


// posts.stream().map()
// -> 형변환 목적으로 map 을 돌림 ( 기존에 post -> PostWithUserRes ) 으로.

// toList() 하면 PostWithUserRes 로 바꿀수 있다.