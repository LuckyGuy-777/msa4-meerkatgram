package com.msa4meerkatgram.domain.post.mapper;


import com.msa4meerkatgram.domain.post.entities.Post;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper // 메퍼 인터페이스로 사용 하기위한 어노테이션
public interface PostMapper {

    // 메퍼세팅
    List<Post> getPagination(int limit, int offset);

    long getTotal();




}


//