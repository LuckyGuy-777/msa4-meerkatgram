package com.msa4meerkatgram.global.util.file;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;


@ConfigurationProperties(prefix = "file")
public record FileConfig(
        String serverUri
        , String storagePath
        , String profilePath
        , String postPath
        , List<String> allowExtensionList
        ) {

}




// prefix = "file" : yaml 의 file의 내용을 가져온다
/*
 보통 @ConfigurationProperties(prefix = "")
은, yaml 의 요소들을 가져다 쓰는 역할을 하는듯함

*       String serverUri
        ,String storagePath
        ,String profilePath
        ,String postPath

        이 것들은, yaml 의 file 의 내용들을 담는 목적
*
*

 환경변수 설정 객체
* */