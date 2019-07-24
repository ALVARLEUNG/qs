package com.myproject.qs.qs.common.schedule;

import com.myproject.qs.qs.service.QSEamilDeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduleJob {

    @Autowired
    QSEamilDeliveryService qsEamilDeliveryService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void sendInvitationEmailToUsers() {
        qsEamilDeliveryService.inviteAllUser();
    }
}
