package com.msa4meerkatgram.domain.post.controllers;

import com.msa4meerkatgram.domain.post.requests.PostIndexReq;
import com.msa4meerkatgram.domain.post.responses.PostIndexRes;
import com.msa4meerkatgram.domain.post.services.PostService;
import com.msa4meerkatgram.global.response.GlobalRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor // 필드 만 생성해도, 해당하는 생성자를 만들어주는 어노테이션
@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    // 1. 먼저 해야할일.
    // GetMapping 진행
    @GetMapping("/posts")
    public ResponseEntity<GlobalRes<PostIndexRes>> index(PostIndexReq postIndexReq) {

        PostIndexRes postIndexRes = postService.index(postIndexReq);

        return ResponseEntity.status(200).body(
                GlobalRes.<PostIndexRes>builder()
                        .code("00")
                        .message("정상처리")
                        .data(postIndexRes)
                        .build()
        );
    }
}



/*
*  GlobalRes.<String>builder()
           .code("00")
           .message("정상처리")
           .data(String.format("page : %d, limit: %d", req.page(), req.limit()))
           .build()
*
* 위처럼, 체이닝 메소드가 가능한 이유는,
* GlobalRes 클래스에,  @Builder 어노테이션이 존재하기 때문이다.
*
* 그리고 String 타입으로 형변환 해서  <String> ,
*  데이터를 처리해야 한다.
*
* */