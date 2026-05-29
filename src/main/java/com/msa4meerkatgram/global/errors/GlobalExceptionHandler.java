package com.msa4meerkatgram.global.errors;

import com.msa4meerkatgram.global.errors.custom.InvalidTokenException;
import com.msa4meerkatgram.global.errors.custom.NotRegisteredException;
import com.msa4meerkatgram.global.response.GlobalRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {



    @ExceptionHandler(NotRegisteredException.class)
    public ResponseEntity<GlobalRes<String>> notRegisteredHandle(NotRegisteredException e){

        return ResponseEntity.status(400).body(

                GlobalRes.<String>builder()
                        .code("E01")
                        .message("로그인 에러.")
                        .data(e.getMessage())
                        .build()
        );
    }


    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<GlobalRes<String>> invalidTokenHandle(InvalidTokenException e){

        return ResponseEntity.status(400).body(

                GlobalRes.<String>builder()
                        .code("E04")
                        .message("토큰 이상")
                        .data(e.getMessage())
                        .build()
        );
    }






    // 매개변수 타입 불일치 예외를 처리함
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<GlobalRes<String>> MethodArgumentTypeMismatchHandle(MethodArgumentTypeMismatchException e){
        // 특정필드 하나에 대한 에러가 발생했을때, validation exception 이 반환된다.
        // 프로퍼티 하나에 대해서만 반환됨.

        // 400 번 에러가 나왔을때, 내용을 적어주는거 같음.
        return ResponseEntity.status(400).body(
                // GlobalRes의 builder() 를 하는데, 데이터타입을 String 으로 해준다
                // 라는 내용
                GlobalRes.<String>builder()
                        .code("E21")
                        .message("요청 파라미터에 이상이 있습니다.")
                        .data(String.format("%s : 필드를 확인해 주세요", e.getName()))
                        .build()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GlobalRes<List<String>>> MethodArgumentNotValidHandle(MethodArgumentNotValidException e) {
        // 400 번 에러가 나왔을때, 내용을 적어주는거 같음.
        return ResponseEntity.status(400).body(
                // GlobalRes의 builder() 를 하는데, 데이터타입을 String 으로 해준다
                // 라는 내용
                // 데이터 여러개를 가져 오기 위해서, List 배열에 String 을 씀
                GlobalRes.<List<String>> builder()
                        .code("E21")
                        .message("요청 파라미터에 이상이 있습니다.")
                        .data(
                                e.getBindingResult()
                                        .getAllErrors()
                                        .stream()
                                        .map(item -> String.format("%s : 잘못된 값입니다. ", item.getObjectName()))
                                        .toList()
                        )
                        .build()
        );

    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<GlobalRes<String>> othersHandle(Exception e) {
        log.error(String.format(
                "시스템 에러: %s\n  %s"
                , e.getMessage()
                ,Arrays.toString(e.getStackTrace())
                )
        );

        return ResponseEntity.status(500).body(
                GlobalRes.<String>builder()
                        .code("E99")
                        .message("시스템 에러")
                        .data("현재 서비스 이용이 불가합니다. 잠시후 다시 시도해 주십시오")
                        .build()
        );

    }
}

// 400 번에, E21
// E21은, BAD REQUEST ERROR 라고함.

/*
* 에러 처리를 하는 클래스라는 걸 알리려면,
* @RestControllerAdvice 를 사용한다.
*
*
* 에러 로그를 남기기 위한
* @Slf4j 어노테이션을 사용함.
*
*
* String.format 는,
* %s 나 %d 같은 포멧 문자를 사용하기위해서 사용
*
* e.getStackTrace()
* 상세정보. 에러가 어떤 파일에 어떤라인에서 발생했는걸
* 상세하게 표현해주는 역할을함.
*
* */