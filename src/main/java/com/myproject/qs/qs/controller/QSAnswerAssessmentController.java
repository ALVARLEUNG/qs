package com.myproject.qs.qs.controller;

import com.myproject.qs.qs.common.utils.ResponseEntityUtil;
import com.myproject.qs.qs.dto.QSAnswerAssessmentDto;
import com.myproject.qs.qs.dto.QSAnswerUserDto;
import com.myproject.qs.qs.model.QSAssessment;
import com.myproject.qs.qs.service.QSAnswerAssessmentService;
import com.myproject.qs.qs.vo.QSAnswerDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(path = "qs/answerAssessment")
public class QSAnswerAssessmentController {

    @Autowired
    QSAnswerAssessmentService qsAnswerAssessmentService;

    @GetMapping("/findAssessment/{assessmentId}")
    @ResponseBody
    public ResponseEntity findAssessment (@PathVariable("assessmentId") String assessmentId) {
        QSAssessment qsAssessment = qsAnswerAssessmentService.findAssessment(assessmentId);
        return ResponseEntityUtil.success(qsAssessment);
    }

    @GetMapping("/findAnswerDetail/{answerUserId}")
    @ResponseBody
    public ResponseEntity findAnswerDetail (@PathVariable("answerUserId") String answerUserId) {
        List<QSAnswerDetailVo> qsAnswerDetailVos = qsAnswerAssessmentService.findAnswerDetail(answerUserId);
        return ResponseEntityUtil.success(qsAnswerDetailVos);
    }

    @PostMapping(value = "/checkAnswerUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity checkAnswerUser (@RequestBody QSAnswerUserDto qsAnswerUserDto) {
        return ResponseEntityUtil.success(qsAnswerAssessmentService.checkAnswerUser(qsAnswerUserDto));
    }

    @PostMapping(value = "/saveAnswerDetail", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity saveAnswerDetail (@RequestBody QSAnswerAssessmentDto qsAnswerAssessmentDto) {
        qsAnswerAssessmentService.saveAnswerDetail(qsAnswerAssessmentDto);
        return ResponseEntityUtil.success(HttpStatus.OK);
    }

    @PostMapping(value = "/checkUnsubscribe", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity checkUnsubscribe (@RequestBody QSAnswerUserDto qsAnswerUserDto) {
        return ResponseEntityUtil.success(qsAnswerAssessmentService.checkUnsubscribe(qsAnswerUserDto));
    }

    @GetMapping("exportPdf")
    public void exportPdf(HttpServletResponse response, String answerUserId) throws IOException {
        byte[] fileBytes = qsAnswerAssessmentService.exportPdf(answerUserId);
        response.setContentType("application/pdf");
        response.setHeader("Content-disposition", "");
        response.setContentLength(fileBytes.length);
        response.getOutputStream().write(fileBytes);
    }
}
