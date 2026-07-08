package com.msa4meerkatgram.domain.post.repositories;


import com.msa4meerkatgram.domain.post.entities.Post;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.msa4meerkatgram.domain.post.entities.QPost.post;
import static com.msa4meerkatgram.domain.user.entities.QUser.user;

// QClass 관련 임포트. (ide 가 QPost(QClass)를 빌드시 생성해줌)
// 컴파일 시 -> build 의 , entities 에, QPost 가 생기는 방식


@Repository
@RequiredArgsConstructor
public class PostQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    // select *
    // from posts
    //      join users
    //          on posts.user_id = user.id
    // where deleted_at is null
    // order by created_at desc, id asc
    // limit ? offset ?
    public List<Post> pagination(int offset, int limit) {

        return jpaQueryFactory
                .selectFrom(post)
                .join(post.user, user).fetchJoin()
                .orderBy(post.createdAt.desc(), post.id.desc())
                .limit(limit)
                .offset(offset)
                .fetch();
        // n+1 문제 해결방법
        // left join 으로 다 가져오고 해결하는것 (fetchJoin() )
        // 자바쪽에서 fetch join 으로 객체형태로 만들어 주는것

        // 총 2번만 데이터를 가져와서 설정하는 방법은
        // yaml 에서 설정

        // where deleted_at is null <- 소프트 딜리트는 where절을
        // 구태어 적어주지 않아도 된다.

        //  .orderBy(post.createdAt.desc(), post.id.desc())
        // 생성시간으로 내림차순을 하고, 아이디로 내림차순을 한다.?

    }
}
