package com.meksula.chat.repository;

import com.meksula.chat.domain.registration.verification.VerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @Author
 * Karol Meksuła
 * 21-07-2018
 * */

public interface TmpVerificationCodeRepository extends JpaRepository<VerificationEntity, Long> {
    Optional<VerificationEntity> findByUserId(long userId);
}
