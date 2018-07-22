package com.meksula.chat.domain.registration.verification;

import lombok.Getter;

import javax.persistence.*;

/**
 * @Author
 * Karol Meksuła
 * 21-07-2018
 * */

@Getter
@Entity
@Table(name = "verif")
public class VerificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long userId;
    private String verificationCode;

    public VerificationEntity(long userId, String verificationCode) {
        this.userId = userId;
        this.verificationCode = verificationCode;
    }

    public VerificationEntity() {}

}
