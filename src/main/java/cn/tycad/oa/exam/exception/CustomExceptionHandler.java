package cn.tycad.oa.exam.exception;

import cn.tycad.oa.exam.model.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author: YY
 * @Date: 2019/3/13
 * @Description:
 */
@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity<Result> handler(Exception e) {
        if (e instanceof MethodArgumentNotValidException){
            List<ObjectError> allErrors = ((MethodArgumentNotValidException) e).getBindingResult().getAllErrors();
            StringBuffer sb = new StringBuffer();
            for (ObjectError allError : allErrors) {
                sb.append("[" + allError.getDefaultMessage() + "]");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.error(sb.toString()));
        }

        if (e instanceof BusinessException) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.businessError(((BusinessException) e).getExceptionInfoEnum()));
        } else {
            log.debug("exception:{}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.error(e.getMessage()));
        }
    }

}
