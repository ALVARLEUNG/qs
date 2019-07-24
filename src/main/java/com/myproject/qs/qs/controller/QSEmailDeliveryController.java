package com.myproject.qs.qs.controller;

import com.myproject.qs.qs.common.utils.ResponseEntityUtil;
import com.myproject.qs.qs.model.QSEmail;
import com.myproject.qs.qs.service.QSEamilDeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "qs/email")
public class QSEmailDeliveryController {

    @Autowired
    QSEamilDeliveryService qsEamilDeliveryService;

    @GetMapping("/findEmailTemplate")
    @ResponseBody
    public ResponseEntity findEmailTemplate () {
        return ResponseEntityUtil.success(qsEamilDeliveryService.findEmailTemplate());
    }

    @PostMapping(path = "/updateEmailTemplate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateUserInfo (@RequestBody QSEmail qsEmail) {
        return ResponseEntityUtil.success(qsEamilDeliveryService.updateEmailTemplate(qsEmail));
    }
}
