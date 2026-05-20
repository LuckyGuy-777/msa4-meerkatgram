package com.msa4meerkatgram.domain.post.entities;

import lombok.Builder;
import lombok.Getter;

//  이 클래스는 DTO 임
// DB에서 데이터를 전달받고 저장하는 클래스.

@Getter
@Builder
public class Post {
    private Long id;
    private Long userId;
    private String content;
    private String image;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;


}
