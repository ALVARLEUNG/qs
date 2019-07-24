package com.myproject.qs.qs.controller;

import com.myproject.qs.qs.common.utils.ResponseEntityUtil;
import com.myproject.qs.qs.dto.QSLoginInfoDto;
import com.myproject.qs.qs.model.QSAdmin;
import com.myproject.qs.qs.service.QSLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.awt.*;

@Controller
@RequestMapping(path = "qs/login")
public class QSLoginController {

    @Autowired
    QSLoginService qsLoginService;

    @PostMapping(value = "/checkLogin", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity checkLogin(@RequestBody QSLoginInfoDto qsLoginInfoDto, HttpSession session) {
        return ResponseEntityUtil.success(qsLoginService.checkLogin(qsLoginInfoDto, session));

    }
}
