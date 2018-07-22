package com.meksula.chat.domain.registration.verification;

import com.meksula.chat.domain.mail.Mail;
import com.meksula.chat.domain.mail.MailSender;
import com.meksula.chat.domain.user.ApplicationUser;
import com.meksula.chat.domain.user.ChatUser;
import com.meksula.chat.repository.ChatUserRepository;
import com.meksula.chat.repository.TmpVerificationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @Author
 * Karol Meksuła
 * 21-07-2018
 * */

@Service
public class ChatUserVerification extends UserVerification {
    private TmpVerificationCodeRepository verificationCodeRepository;
    private ChatUserRepository chatUserRepository;
    private MailSender mailSender;

    @Autowired
    public void setVerificationCodeRepository(TmpVerificationCodeRepository verificationCodeRepository) {
        this.verificationCodeRepository = verificationCodeRepository;
    }

    @Autowired
    public void setChatUserRepository(ChatUserRepository chatUserRepository) {
        this.chatUserRepository = chatUserRepository;
    }

    @Autowired
    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    protected VerificationEntity saveEntity(VerificationEntity entity) {
        return verificationCodeRepository.save(entity);
    }

    @Override
    protected Optional<VerificationEntity> fetchEntity(long userId) {
        return verificationCodeRepository.findByUserId(userId);
    }

    @Override
    protected void enableAccount(long userId) {
        ChatUser chatUser = chatUserRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Cannot find user with userId: " + userId));

        chatUserRepository.delete(chatUser);

        chatUser.setEnable(true);
        chatUserRepository.save(chatUser);
    }

    @Override
    protected boolean sendEmail(ApplicationUser applicationUser, VerificationEntity entity) {
        return mailSender.sendMail(Mail.MailType.VERIFICATION, applicationUser, entity);
    }

    @Override
    protected void removeEntity(long userId) {
        verificationCodeRepository.delete(fetchEntity(userId).get());
    }
}
