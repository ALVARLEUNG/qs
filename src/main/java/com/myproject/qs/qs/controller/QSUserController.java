package com.myproject.qs.qs.controller;

import com.myproject.qs.qs.common.constant.HttpCode;
import com.myproject.qs.qs.common.utils.ResponseEntityUtil;
import com.myproject.qs.qs.model.QSUser;
import com.myproject.qs.qs.service.QSUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping(path = "qs/user")
public class QSUserController {

    @Autowired
    QSUserService qsUserService;

    @GetMapping("/findAllUsers")
    @ResponseBody
    public ResponseEntity findAllUsers () {
        return ResponseEntityUtil.success(qsUserService.findAllUsers());
    }

    @PostMapping(value = "/updateUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateUser (@RequestBody QSUser qsUser) {
        return ResponseEntityUtil.success(qsUserService.updateUser(qsUser));
    }

    @PostMapping(value = "/uploadUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity uploadUser(MultipartFile file) throws Exception {
         qsUserService.readInfoFromExcel(file);
        return ResponseEntityUtil.success(HttpCode.SUCCESS);
    }

    @PostMapping(value = "/searchUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity searchUser(@RequestBody QSUser qsUser) throws Exception {
        List<QSUser> qsUserList = qsUserService.searchUserByConditions(qsUser);
        return ResponseEntityUtil.success(qsUserList);
    }

    @PostMapping(value = "/unsubscribed", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity userUnsubscribed(@RequestBody QSUser qsUser) throws Exception {
         qsUserService.userUnsubscribed(qsUser);
        return ResponseEntityUtil.success(HttpCode.SUCCESS);
    }
}
