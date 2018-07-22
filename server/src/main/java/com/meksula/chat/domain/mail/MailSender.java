package com.meksula.chat.domain.mail;

import com.meksula.chat.domain.user.ApplicationUser;

/**
 * @Author
 * Karol Meksuła
 * 22-07-2018
 * */

public interface MailSender {
    boolean sendMail(Mail.MailType mailType, ApplicationUser applicationUser, Object attachment);
}
