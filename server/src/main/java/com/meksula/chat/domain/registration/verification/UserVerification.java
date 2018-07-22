package com.meksula.chat.domain.registration.verification;

import com.meksula.chat.domain.user.ApplicationUser;

import java.util.Optional;

/**
 * @Author
 * Karol Meksuła
 * 21-07-2018
 * */

public abstract class UserVerification {

    public final boolean makeVerificationProcess(ApplicationUser applicationUser) {
        String code = CodeGenerator.generateCode(20);
        VerificationEntity verificationEntity = saveEntity(new VerificationEntity(applicationUser.getUserId(), code));
        return sendEmail(applicationUser, verificationEntity) && verificationEntity != null;
    }

    public final boolean verify(final VerificationEntity entity) {
        final VerificationEntity verificationEntity = fetchEntity(entity.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("No verification process for user: " + entity.getUserId()));

        boolean dec = entity.getUserId() == verificationEntity.getUserId()
                && entity.getVerificationCode().equals(verificationEntity.getVerificationCode());

        if (dec) {
            enableAccount(verificationEntity.getUserId());
            removeEntity(verificationEntity.getUserId());
        }

        return dec;
    }

    protected abstract VerificationEntity saveEntity(VerificationEntity entity);

    protected abstract Optional<VerificationEntity> fetchEntity(long userId);

    protected abstract void enableAccount(long userId);

    protected abstract boolean sendEmail(ApplicationUser applicationUser,  VerificationEntity verificationEntity);

    protected abstract void removeEntity(long userId);

}
