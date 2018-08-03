package com.meksula.chat.controller;

import com.meksula.chat.domain.files.DefaultFileExchange;
import com.meksula.chat.domain.files.FileExchange;
import com.meksula.chat.domain.user.ChatUser;
import com.meksula.chat.domain.user.ProfilePreferences;
import com.meksula.chat.repository.ProfilePreferencesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;

/**
 * @Author
 * Karol Meksuła
 * 03-08-2018
 * */

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {
    private ProfilePreferencesRepository profilePreferencesRepository;
    private FileExchange fileExchange;

    public ProfileController() {
        this.fileExchange = new DefaultFileExchange();
    }

    @Autowired
    public void setProfilePreferencesRepository(ProfilePreferencesRepository profilePreferencesRepository) {
        this.profilePreferencesRepository = profilePreferencesRepository;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ProfilePreferences getProfile(Authentication authentication) {
        ChatUser chatUser = (ChatUser) authentication.getPrincipal();
        return profilePreferencesRepository.findByProfileUsername(chatUser.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Profile not found.\nUsername: " + chatUser.getUsername()));
    }

    @PostMapping("/avatar")
    @ResponseStatus(HttpStatus.CREATED)
    public boolean setAvatar(@RequestParam("pic")MultipartFile multipartFile, Authentication authentication) {
        return fileExchange.uploadPicture(multipartFile, (ChatUser) authentication.getPrincipal());
    }

    @GetMapping(value = "/avatar/{username}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public byte[] getAvatar(@PathVariable("username") String username) {
        return fileExchange.getPicture(username);
    }

}
