package com.msa4meerkatgram.global.response;


public record GlobalRes<T>(
        String code
        ,String message,
        T data
) {
    public static<T> GlobalRes<T> from(String code, String message, T data) {
        return new GlobalRes<T>(code, message,  data);
    }
}


// 외부에서 타입을 받는, 제네릭스를 지정하려면..
/*
* GlobalRes 처럼, 메소드 선언부에,  를 넣어줘야하고,
* 프로퍼티에 T 를 주어야한다.
*
* */