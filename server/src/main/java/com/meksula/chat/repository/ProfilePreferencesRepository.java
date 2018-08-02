package com.meksula.chat.repository;

import com.meksula.chat.domain.user.ProfilePreferences;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface ProfilePreferencesRepository extends CrudRepository<ProfilePreferences, Long> {
    Optional<ProfilePreferences> findByProfileUsername(String profilesUsername);
}
