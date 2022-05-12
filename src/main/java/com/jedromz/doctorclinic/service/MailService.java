package com.jedromz.doctorclinic.service;

import com.jedromz.doctorclinic.model.dto.NotificationEmail;
import org.springframework.scheduling.annotation.Async;

public interface MailService {
    @Async
    void sendMail(NotificationEmail notificationEmail);
}
