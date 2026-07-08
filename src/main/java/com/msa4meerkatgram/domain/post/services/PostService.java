package com.msa4meerkatgram.domain.post.services;

import com.msa4meerkatgram.domain.post.entities.Post;
import com.msa4meerkatgram.domain.post.repositories.PostQueryRepository;
import com.msa4meerkatgram.domain.post.repositories.PostRepository;
import com.msa4meerkatgram.domain.post.requests.PostIndexReq;
import com.msa4meerkatgram.domain.post.responses.PostIndexRes;
import com.msa4meerkatgram.domain.post.responses.PostWithUserRes;
import com.msa4meerkatgram.global.errors.custom.DeletedRecordException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// Service 코드는, 마이바티스 와 소통하는 경향이다.
// Service 코드는, 매퍼 인터페이스를 가진다.

@Service
@RequiredArgsConstructor // 생성자 대체 해주는 어노테이션
public class PostService {

    private final PostRepository postRepository;
    private final PostQueryRepository postQueryRepository;

    public PostIndexRes index(PostIndexReq postIndexReq) {

        int offset = (postIndexReq.page() -1) * postIndexReq.limit();

        // 특정 페이지의, 게시글 조회
        List<Post> result = postQueryRepository.pagination(offset,postIndexReq.limit());

        // 토탈 획득

        // postRepository 의 count() 메소드 사용.
        long total = postRepository.count();
        boolean lastPage = offset + postIndexReq.limit() >= total;


        // 게시글 조회 한걸, 컨트롤러에 전달함.
        return PostIndexRes.from(total,lastPage,result);

    }

    public PostWithUserRes show(long id) {
            Post result = postRepository.findById(id)
                .orElseThrow(() -> new DeletedRecordException("이미 삭제된 게시글 입니다."));

            return PostWithUserRes.from(result);
    }
}
