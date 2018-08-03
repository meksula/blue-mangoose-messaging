package com.meksula.chat.domain.files;

import com.meksula.chat.domain.user.ApplicationUser;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author
 * Karol Meksuła
 * 03-08-2018
 * */

public interface FileExchange {
    boolean uploadPicture(MultipartFile file, ApplicationUser applicationUser);

    byte[] getPicture(String username);

    String getPicturePath();
}
