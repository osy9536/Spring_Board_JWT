package com.board.spring_board_jwt.aop;


import com.board.spring_board_jwt.dto.ResponseMsgDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// @Valid 예외처리를 전역 컨트롤러로 처리
@RestControllerAdvice   // 전역 설정을 위한 annotation
public class ExceptionAdvisor {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> processValidationError(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();

        ResponseMsgDto ok = new ResponseMsgDto("",HttpStatus.OK.value());
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            ok = ResponseMsgDto.builder()
                    .msg(fieldError.getDefaultMessage())
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(ok);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> processWithdrawalError(IllegalArgumentException exception) {
        String exceptionMessage = exception.getMessage();
        ResponseMsgDto no = ResponseMsgDto.builder()
                .msg(exceptionMessage)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(no);
    }
}