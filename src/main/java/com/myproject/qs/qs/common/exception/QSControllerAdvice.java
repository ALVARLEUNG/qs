package com.myproject.qs.qs.common.exception;

import com.myproject.qs.qs.common.response.ResultEnum;
import com.myproject.qs.qs.common.utils.ResponseEntityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
/*拦截异常，全局异常捕捉
　@ControllerAdvice注解将作用在所有注解了@RequestMapping的控制器的方法上
　　@ExceptionHandler：用于全局处理控制器里的异常。
 * */
@ControllerAdvice
public class QSControllerAdvice {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity exceptionHandler(Exception e, HttpServletRequest req) {
        ResponseEntity responseEntity;
        if (e instanceof CustomException) {
            responseEntity = ResponseEntityUtil.responseCustomException(((CustomException) e).getCode(), e.getMessage(), ((CustomException) e).getData());
        } else {
            e.printStackTrace();
            responseEntity = ResponseEntityUtil.responseGlobalException(ResultEnum.UnknownException.getCode(), ResultEnum.UnknownException.getMessage());
        }
        return responseEntity;
    }
}
