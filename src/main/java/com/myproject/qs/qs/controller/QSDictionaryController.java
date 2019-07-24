package com.myproject.qs.qs.controller;

import com.myproject.qs.qs.common.utils.ResponseEntityUtil;
import com.myproject.qs.qs.model.QSDictionary;
import com.myproject.qs.qs.service.QSDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "qs/dictionary")
public class QSDictionaryController {

    @Autowired
    QSDictionaryService qsDictionaryService;

    @GetMapping("/findDictionary")
    @ResponseBody
    public ResponseEntity findDictionary () {
        return ResponseEntityUtil.success(qsDictionaryService.findDictionary());
    }

    @PostMapping(path = "/updateDictionary", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateDictionary (@RequestBody QSDictionary qsDictionary) {
        return ResponseEntityUtil.success(qsDictionaryService.updateDictionary(qsDictionary));
    }
}
