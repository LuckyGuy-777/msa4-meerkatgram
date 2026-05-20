package com.msa4meerkatgram.domain.post.requests;

import jakarta.validation.constraints.Min;

public record PostIndexReq(
        // @Min은, 프로퍼티의 최소값을 검사하는것.
        @Min(value = 1, message = "1이상 숫자만 허용합니다.")
        Integer page,
        @Min(value = 1, message = "1이상 숫자만 허용 합니다.")
        Integer limit
) {

    public PostIndexReq(Integer page, Integer limit) {

        // page가 null 이 아니거나,
        this.page = (page != null && page > 0 ) ? page : 1;
        this.limit = (limit != null && limit > 0) ? limit : 6;
    }
}
