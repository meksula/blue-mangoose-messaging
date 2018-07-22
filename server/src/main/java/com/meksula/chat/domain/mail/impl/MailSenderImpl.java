package com.meksula.chat.domain.mail.impl;

import com.meksula.chat.domain.mail.Mail;
import com.meksula.chat.domain.mail.MailSender;
import com.meksula.chat.domain.user.ApplicationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @Author
 * Karol Meksuła
 * 22-07-2018
 * */

@Service
public class MailSenderImpl implements MailSender {
    private JavaMailSender javaMailSender;
    private TemplateEngine templateEngine;

    @Autowired
    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Autowired
    public void setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public boolean sendMail(Mail.MailType mailType, ApplicationUser applicationUser, Object attachment) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");

        String htmlContent = templateEngine.process(mailType.getTemplate(), mailType.setContext(attachment));

        try {
            messageHelper.setSubject(mailType.getTitle());
            messageHelper.setTo(applicationUser.getEmail());
            messageHelper.setText(htmlContent, true);
        } catch (MessagingException e) {
            return false;
        }

        javaMailSender.send(mimeMessage);

        return true;
    }

}
