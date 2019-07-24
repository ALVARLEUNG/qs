package com.myproject.qs.qs.common.utils;

import com.myproject.qs.qs.common.constant.HttpCode;
import com.myproject.qs.qs.common.response.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseEntityUtil {

    public static ResponseEntity success(Object o) {
        Result<Object> result = new Result<>(o);
        result.setCode(HttpCode.SUCCESS);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    public static ResponseEntity response(Object o, HttpStatus httpStatus) {
        Result<Object> result = new Result<>(o);
        result.setCode(HttpCode.SUCCESS);
        return new ResponseEntity<Object>(result, httpStatus);
    }

    public static ResponseEntity responseWithoutContent(HttpStatus httpStatus) {
        Result<Object> result = new Result<>(HttpCode.SUCCESS);
        return new ResponseEntity<Object>(result, httpStatus);
    }

    public static ResponseEntity responseCustomException(String code, String message, Object data) {
        Result<Object> result = new Result<>(code);
        result.setMsg(message);
        result.setData(data);
        return new ResponseEntity<Object>(result, HttpStatus.OK);
    }

    public static ResponseEntity responseGlobalException(String code, String message) {
        Result<Object> result = new Result<>(code);
        result.setMsg(message);
        return new ResponseEntity<Object>(result, HttpStatus.OK);
    }
}
