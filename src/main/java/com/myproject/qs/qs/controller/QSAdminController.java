package com.myproject.qs.qs.controller;

import com.myproject.qs.qs.common.utils.ResponseEntityUtil;
import com.myproject.qs.qs.model.QSAdmin;
import com.myproject.qs.qs.service.QSAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path = "qs/admin")
public class QSAdminController {

    @Autowired
    QSAdminService qsAdminService;

    @GetMapping("/findUserInfo")
    @ResponseBody
    public ResponseEntity findUserInfo () {
        return ResponseEntityUtil.success(qsAdminService.findUserInfo());
    }

    @PostMapping(path = "/updateUserInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateUserInfo (@RequestBody QSAdmin qsAdmin) {
        return ResponseEntityUtil.success(qsAdminService.updateUserInfo(qsAdmin));
    }

    @PostMapping(path = "/deleteUsers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteUsers (@RequestBody List<QSAdmin> qsAdmins) {
        return ResponseEntityUtil.success(qsAdminService.deleteUsers(qsAdmins));
    }


}
