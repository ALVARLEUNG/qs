package com.myproject.qs.qs.controller;

import com.myproject.qs.qs.common.utils.ResponseEntityUtil;
import com.myproject.qs.qs.dto.QSAnswerUserDto;
import com.myproject.qs.qs.service.QSAnswerUserService;
import com.myproject.qs.qs.vo.QSAnswerUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path = "qs/answerUser")
public class QSAnswerUserController {

    @Autowired
    QSAnswerUserService qsAnswerUserService;

    @GetMapping("/findAllAnswerUsers")
    @ResponseBody
    public ResponseEntity findAllAnswerUser () {
        return ResponseEntityUtil.success( qsAnswerUserService.findAllAnswerUser());
    }

    @PostMapping(value = "/searchAnswerUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity searchAnswerUser(@RequestBody QSAnswerUserDto qsAnswerUserDto) throws Exception {
        List<QSAnswerUserVo> qsAnswerUserVos = qsAnswerUserService.searchAnswerUserByConditions(qsAnswerUserDto);
        return ResponseEntityUtil.success(qsAnswerUserVos);
    }

    @PostMapping(value = "/remindUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity reminder(@RequestBody List<String> qsAnswerUserIds) throws Exception {
        qsAnswerUserService.remindUsers(qsAnswerUserIds);
        return ResponseEntityUtil.success(HttpStatus.OK);
    }

}
