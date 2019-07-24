package com.myproject.qs.qs.controller;

import com.myproject.qs.qs.common.utils.ResponseEntityUtil;
import com.myproject.qs.qs.model.QSAssessment;
import com.myproject.qs.qs.service.QSAssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping(path = "qs/assessment")
public class QSAssessmentController {

    @Autowired
    QSAssessmentService qsAssessmentService;

    @GetMapping("/findAllAssessments")
    @ResponseBody
    public ResponseEntity findAllAssessments () {
        return ResponseEntityUtil.success(qsAssessmentService.findAllAssessments());
    }

    @PostMapping(value = "/deleteAssessment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteAssesssment (@RequestBody QSAssessment qsAssessment) {
        qsAssessmentService.deleteAssessment(qsAssessment);
        return ResponseEntityUtil.success(HttpStatus.OK);
    }

    @PostMapping(value = "/confirmAssessment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity confirmAssesssment (@RequestBody QSAssessment qsAssessment) {
        qsAssessmentService.confirmAssessment(qsAssessment);
        return ResponseEntityUtil.success(HttpStatus.OK);
    }

    @PostMapping(value = "/uploadAssessment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity uploadAssessment(MultipartFile file) throws Exception {
       QSAssessment assessment = qsAssessmentService.readInfoFromExcel(file);
        return ResponseEntityUtil.success(assessment);
    }

    @GetMapping("/findAssessmentType")
    @ResponseBody
    public ResponseEntity findAssessmentType () {
        List<String> assessmentTypes = qsAssessmentService.findAssessmentType();
     return ResponseEntityUtil.success(assessmentTypes);
    }

    @PostMapping(value = "/saveAssessment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity saveAssessment(@RequestBody QSAssessment qsAssessment) {
        qsAssessmentService.saveAssessment(qsAssessment);
        return ResponseEntityUtil.success(HttpStatus.OK);
    }
}
