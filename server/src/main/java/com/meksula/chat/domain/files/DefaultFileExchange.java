package com.meksula.chat.domain.files;

import com.meksula.chat.domain.user.ApplicationUser;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;

/**
 * @Author
 * Karol Meksuła
 * 03-08-2018
 * */

public class DefaultFileExchange implements FileExchange {
    private String picturePath;

    public DefaultFileExchange() {
        String username = System.getProperty("user.name");
        picturePath = "/home/" + username + "/uploads/";
    }

    @Override
    public boolean uploadPicture(MultipartFile file, ApplicationUser applicationUser) {
        try {
            byte[] bytes = file.getBytes();
            FileUtils.writeByteArrayToFile(new File(picturePath + applicationUser.getUsername()), bytes);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public byte[] getPicture(String username) {
        try {
            InputStream inputStream = new FileInputStream(new File(picturePath + username));
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            return new byte[0];
        }
    }

    @Override
    public String getPicturePath() {
        return picturePath;
    }

}
