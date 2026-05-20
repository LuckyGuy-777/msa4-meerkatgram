package com.msa4meerkatgram.global.response;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GlobalRes<T> {
    private String code;
    private String message;
    private T data;
}


// 외부에서 타입을 받는, 제네릭스를 지정하려면..
/*
* GlobalRes<T> 처럼, 메소드 선언부에, <T> 를 넣어줘야하고,
* 프로퍼티에 T 를 주어야한다.
*
* */