package com.meksula.chat.domain.mail;

import com.meksula.chat.domain.registration.verification.VerificationEntity;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;

/**
 * @Author
 * Karol Meksuła
 * 22-07-2018
 * */

public class Mail {

    public enum MailType {
        VERIFICATION {
            @Override
            public String getTemplate() {
                return "verification.html";
            }

            @Override
            public IContext setContext(Object attachment) {
                VerificationEntity verificationEntity = (VerificationEntity) attachment;

                String link = new StringBuilder()
                        .append("http://51.38.129.50:8060/api/v1/verification")
                        .append("/")
                        .append(verificationEntity.getUserId())
                        .append("/")
                        .append(verificationEntity.getVerificationCode())
                        .toString();

                Context context = new Context();
                context.setVariable("userId", verificationEntity.getUserId());
                context.setVariable("code", verificationEntity.getVerificationCode());
                context.setVariable("link", link);

                return context;
            }

            @Override
            public String getTitle() {
                return "Weryfikacja konta";
            }
        };

        public abstract String getTemplate();

        public abstract IContext setContext(Object attachment);

        public abstract String getTitle();

    }

}


