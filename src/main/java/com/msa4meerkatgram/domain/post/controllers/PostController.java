package com.msa4meerkatgram.domain.post.controllers;

import com.msa4meerkatgram.domain.post.entities.Post;
import com.msa4meerkatgram.domain.post.requests.PostIndexReq;
import com.msa4meerkatgram.domain.post.responses.PostIndexRes;
import com.msa4meerkatgram.domain.post.services.PostService;
import com.msa4meerkatgram.global.response.GlobalRes;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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


    @GetMapping("/posts/{id}")
    public ResponseEntity<GlobalRes<Post>> show(
            @Min(value = 1, message = "1이상 숫자만 허용") @PathVariable long id
    ) {
        Post result = postService.show(id);

        return ResponseEntity.status(200).body(
                GlobalRes.<Post>builder()
                        .code("00")
                        .message("게시글 상세 정상 처리")
                        .data(result)
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
*
*  ResponseEntity 는, 우리가 세팅하지 않았던 여러 값들을
*  세팅해야하는데, 이런것들을 ResponseEntity 를 통해서,
*  스프링부트가 자동으로 세팅하는 역할.
*
*  body 부분에, 세팅해줘야하는 형식이 json 형태였고
*  그것을 규격화 하여서, 응답하기 위해서 GlobalRes
*  를 가진다.
*
*  response 할때 편하게, 응답하기 위해서 사용함.
*
* */