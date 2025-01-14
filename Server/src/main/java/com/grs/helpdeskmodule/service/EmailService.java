package com.grs.helpdeskmodule.service;

import com.grs.helpdeskmodule.config.MailConfiguration;
import com.grs.helpdeskmodule.dto.MailBody;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final MailConfiguration mailSender;

    @Value("${spring.mail.username}")
    private String mailFrom;

    public void sendEmail(MailBody mailBody) {
        try {
            MimeMessage mimeMessage = mailSender.getJavaMailSender().createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setFrom(mailFrom);
            mimeMessageHelper.setTo(mailBody.getTo());
            mimeMessageHelper.setSubject(mailBody.getSubject());
            mimeMessageHelper.setText(mailBody.getBody(), true);

            mailSender.getJavaMailSender().send(mimeMessage);

        } catch (MessagingException e) {
            e.fillInStackTrace();
        }
    }
}
