package com.meksula.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
@RequestMapping("/download")
public class DownloadClient {

    @GetMapping(produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void downloadFile(HttpServletResponse response) throws IOException {
        response.setContentType("application/jar");
        response.setHeader("Content-Disposition", "attachment; filename=\"blue-mangoose-1.0.jar\"");
        FileInputStream inputStream = new FileInputStream(new File("/home/karol/download/blue-mangoose-1.0.jar"));

        int nRead;
        while((nRead = inputStream.read()) != -1) {
            response.getWriter().write(nRead);
        }

    }

}
