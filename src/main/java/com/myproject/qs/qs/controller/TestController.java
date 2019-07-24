package com.myproject.qs.qs.controller;

import com.myproject.qs.qs.common.utils.ResponseEntityUtil;
import com.myproject.qs.qs.model.QSAssessment;
import com.myproject.qs.qs.model.QSUserCategory;
import com.myproject.qs.qs.repository.QSAssessmentRepository;
import com.myproject.qs.qs.repository.QSUserCategoryRepository;
import com.myproject.qs.qs.service.QSEamilDeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "qs")
public class TestController {

    @Autowired
    QSUserCategoryRepository qsUserCategoryRepository;

    @Autowired
    QSEamilDeliveryService eamilDeliveryService;

    @Autowired
    QSAssessmentRepository qsAssessmentRepository;

    @GetMapping("/test")
    @ResponseBody
    public String Testing () {
        return "Hello Alvar";
    }

    @GetMapping("/test/save")
    @ResponseBody
    public String TestSave() {
        QSUserCategory qsUserCategory = new QSUserCategory();
        qsUserCategory.setCategory("学生");
        qsUserCategoryRepository.save(qsUserCategory);
        return "save success";
    }

    @GetMapping("/test/search")
    @ResponseBody
    public List TestSearch() {
        List<QSUserCategory> qsUserCategories = new ArrayList<>();
        qsUserCategories = qsUserCategoryRepository.findAll();
        return qsUserCategories;
    }

    @GetMapping("/test/email/invitation")
    @ResponseBody
    public String TestInvitaEmail() {
        QSAssessment qsAssessment  = qsAssessmentRepository.findById("4028d48169e3fc4e0169e40072300000").orElse(null);
       return  eamilDeliveryService.invitationEmailDelivery(qsAssessment);
    }

    @GetMapping("/test/email/reminder")
    @ResponseBody
    public String TestReminderEmail() {
        List<String> qsAnswerUsers = new ArrayList<>();
        return  eamilDeliveryService.reminderEmailDelivery(qsAnswerUsers);
    }

    @GetMapping("/test/find/assessment")
    @ResponseBody
    public ResponseEntity findAllAssessment () {
        return ResponseEntityUtil.success(qsAssessmentRepository.findAll());
    }
}
