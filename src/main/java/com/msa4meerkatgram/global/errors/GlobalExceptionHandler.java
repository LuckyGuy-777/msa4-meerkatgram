package com.msa4meerkatgram.global.errors;

import com.msa4meerkatgram.global.errors.constant.CustomErrorCode;
import com.msa4meerkatgram.global.errors.custom.*;
import com.msa4meerkatgram.global.response.GlobalErrorRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;


// 이 클래스에, 내가만든 예외파일을 만든것을 넣어줘야, 예외시에, 내가 작성한 예외처리가 작동함
// 내가 만든 예외사항을 띄우기위해..

//  customErrorCode.name() : 상수명을 tostring() 을 통해 그대로 문자열로 받아옴.


// 내가 필요한 상수 하나만, 인수로 받아오면 됨.
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private ResponseEntity<GlobalErrorRes> generateErrorResponse(CustomErrorCode customErrorCode){
        return ResponseEntity.status(customErrorCode.getHttpStatus())
                .body(GlobalErrorRes.from(customErrorCode.getCode(), customErrorCode.name()));
    }



    @ExceptionHandler(NotRegisteredException.class)
    public ResponseEntity<GlobalErrorRes> notRegisteredHandle(NotRegisteredException e){
        log.debug(CustomErrorCode.NOT_REGISTERED_ERROR.name(),e);
        return this.generateErrorResponse(CustomErrorCode.NOT_REGISTERED_ERROR);
    }



    // 인증에러
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<GlobalErrorRes> authenticationHandle(AuthenticationException e){

        log.debug(CustomErrorCode.UNAUTHENTICATED_ERROR.name(),e);
        return this.generateErrorResponse(CustomErrorCode.UNAUTHENTICATED_ERROR);
    }


    // 권한에러
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<GlobalErrorRes> accessDeniedHandle(AccessDeniedException e){
        log.debug(CustomErrorCode.UNAUTHORIZED_ERROR.name(),e);
        return this.generateErrorResponse(CustomErrorCode.UNAUTHORIZED_ERROR);
    }




    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<GlobalErrorRes> invalidTokenHandle(InvalidTokenException e){

        log.debug(CustomErrorCode.INVALID_TOKEN_ERROR.name(),e);
        return this.generateErrorResponse(CustomErrorCode.INVALID_TOKEN_ERROR);
    }

    @ExceptionHandler(DeletedRecordException.class)
    public ResponseEntity<GlobalErrorRes> deletedRecordHandle(DeletedRecordException e){

        log.debug(CustomErrorCode.NOT_FOUND_DATA_ERROR.name(),e);
        return this.generateErrorResponse(CustomErrorCode.NOT_FOUND_DATA_ERROR);
    }


    @ExceptionHandler(DuplicatedRecordException.class)
    public ResponseEntity<GlobalErrorRes> duplicatedRecordHandle(DuplicatedRecordException e){

        log.debug(CustomErrorCode.DUPLICATED_RECORD_ERROR.name(),e);
        return this.generateErrorResponse(CustomErrorCode.DUPLICATED_RECORD_ERROR);
    }





    // 매개변수 타입 불일치 예외를 처리함
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<GlobalErrorRes> MethodArgumentTypeMismatchHandle(MethodArgumentTypeMismatchException e){

        log.debug(CustomErrorCode.INVALID_PARAMETER_ERROR.name(),String.format("%s : 필드를 확인해 주세요", e.getName()));
        return this.generateErrorResponse(CustomErrorCode.INVALID_PARAMETER_ERROR);


        // 특정필드 하나에 대한 에러가 발생했을때, validation exception 이 반환된다.
        // 프로퍼티 하나에 대해서만 반환됨.

    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GlobalErrorRes> methodArgumentNotValidHandle(MethodArgumentNotValidException e) {
        Map<String, String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField, // 필드명
                        fieldError -> fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() : "유효하지 않은 값입니다.",
                        (existing, replacement) -> existing // 중복 필드가 있을 경우 기존 값 유지
                ));

        log.debug(CustomErrorCode.INVALID_PARAMETER_ERROR.name(),errors);
        return this.generateErrorResponse(CustomErrorCode.INVALID_PARAMETER_ERROR);
    }



    // @ExceptionHandler(MethodArgumentNotValidException.class)
    // public ResponseEntity<GlobalRes<List<String>>> MethodArgumentNotValidHandle(MethodArgumentNotValidException e) {
    //     // 400 번 에러가 나왔을때, 내용을 적어주는거 같음.
    //     return ResponseEntity.status(400).body(
    //             // GlobalRes의 builder() 를 하는데, 데이터타입을 String 으로 해준다
    //             // 라는 내용
    //             // 데이터 여러개를 가져 오기 위해서, List 배열에 String 을 씀
    //             GlobalRes.<List<String>> builder()
    //                     .code("E21")
    //                     .message("요청 파라미터에 이상이 있습니다.")
    //                     .data(
    //                             e.getBindingResult()
    //                                     .getAllErrors()
    //                                     .stream()
    //                                     .map(item -> String.format("%s : 잘못된 값입니다. ", item.getObjectName()))
    //                                     .toList()
    //                     )
    //                     .build()
    //     );
    //
    // }


    @ExceptionHandler(FileManagedException.class)
    public ResponseEntity<GlobalErrorRes> fileManagedHandle(FileManagedException e) {
        log.debug(CustomErrorCode.FILE_MANAGED_ERROR.name(),e);
        return this.generateErrorResponse(CustomErrorCode.FILE_MANAGED_ERROR);
    }




    @ExceptionHandler(Exception.class)
    public ResponseEntity<GlobalErrorRes> othersHandle(Exception e) {
        log.error(
                "시스템 에러:" , e
        );

        log.debug(CustomErrorCode.SYSTEM_ERROR.name(),e);
        return this.generateErrorResponse(CustomErrorCode.SYSTEM_ERROR);

    }


    // SQL 관련 에러들은, 이 예외를 통한다.
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<GlobalErrorRes> sqlHandle(SQLException e) {
        log.error("DB 에러:" , e);

        log.debug(CustomErrorCode.DB_ERROR.name(),e);
        return this.generateErrorResponse(CustomErrorCode.DB_ERROR);

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