package com.msa4meerkatgram.domain.post.services;

import com.msa4meerkatgram.domain.post.entities.Post;
import com.msa4meerkatgram.domain.post.mapper.PostMapper;
import com.msa4meerkatgram.domain.post.requests.PostCreateReq;
import com.msa4meerkatgram.domain.post.requests.PostIndexReq;
import com.msa4meerkatgram.domain.post.responses.PostIndexRes;
import com.msa4meerkatgram.global.errors.custom.DeletedRecordException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// Service 코드는, 마이바티스 와 소통하는 경향이다.
// Service 코드는, 매퍼 인터페이스를 가진다.

@Service
@RequiredArgsConstructor // 생성자 대체 해주는 어노테이션
public class PostService {

    private final PostMapper postMapper;

    public PostIndexRes index(PostIndexReq postIndexReq) {

        int offset = (postIndexReq.page() -1) * postIndexReq.limit();

        // 특정 페이지의, 게시글 조회
        List<Post> posts = postMapper.getPagination(postIndexReq.limit(), offset);

        // 토탈 획득
        long total = postMapper.getTotal();
        boolean lastPage = offset + postIndexReq.limit() >= total;


        // 게시글 조회 한걸, 컨트롤러에 전달함.
        return PostIndexRes.builder()
                .total(total)
                .lastPage(lastPage)
                .posts(posts)
                .build();

          // PostIndexRes 가 리턴타입
    }

    public Post show(long id) {
        Post post = postMapper.findByPk(id);

        if(post == null )
        {
           throw new DeletedRecordException("이미 삭제된 게시글입니다.");
        }

        return post;
    }


    public Post createPost(PostCreateReq postCreateReq) {

    }
}
