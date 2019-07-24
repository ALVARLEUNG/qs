package com.myproject.qs.qs.controller;

import com.myproject.qs.qs.common.utils.ResponseEntityUtil;
import com.myproject.qs.qs.model.QSUserCategory;
import com.myproject.qs.qs.service.QSUserCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path = "qs/category")
public class QSUserCategoryController {

    @Autowired
    QSUserCategoryService qsUserCategoryService;

    @GetMapping("/findAllCategories")
    @ResponseBody
    public ResponseEntity findAllCategories () {
        return ResponseEntityUtil.success(qsUserCategoryService.findAllCategories());
    }

    @PostMapping(value = "/updateCategory", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateCategory (@RequestBody QSUserCategory qsUserCategory) {
        return ResponseEntityUtil.success(qsUserCategoryService.updateCategory(qsUserCategory));
    }

    @PostMapping(value = "/removeCategory", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateCategory (@RequestBody List<QSUserCategory> qsUserCategories) {
        return ResponseEntityUtil.success(qsUserCategoryService.removeCategory(qsUserCategories));
    }

}
