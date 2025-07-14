package com.homework26.controller;

import com.amazonaws.services.s3.model.S3Object;
import com.homework26.api.S3FileManager;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class S3FileController {

    public static final String UPLOAD = "/upload";
    public static final String REDIRECT = "redirect:/";
    public static final String FILE_LIST = "file-list";
    private final S3FileManager s3FileManager;

    @GetMapping(UPLOAD)
    public String showUploadForm() {
        return UPLOAD;
    }

    @PostMapping(UPLOAD)
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {
        try {
            s3FileManager.saveToS3(null, file.getBytes(), file.getOriginalFilename(), file.getContentType());
        } catch (IOException e) {
            log.error("Unable to save file", e);
        }
        return REDIRECT;
    }

    @GetMapping("/")
    public String listFiles(Model model) {
        List<String> files = s3FileManager.listFiles();
        model.addAttribute("files", files);
        return FILE_LIST;
    }

    @GetMapping("/download")
    public void downloadFile(@RequestParam("fileName") String fileName, HttpServletResponse response) {
        try {
            Optional<S3Object> s3Object = s3FileManager.getS3Object(null, fileName);
            if (s3Object.isPresent()) {
                OutputStream outputStream = null;
                outputStream = response.getOutputStream();
                response.addHeader("Content-Disposition", "attachment; filename=%s".formatted(fileName));
                IOUtils.copy(s3Object.get().getObjectContent(), outputStream);
            }
        } catch (IOException e) {
            log.error("Unable to download file", e);
        }
    }

    @GetMapping("/delete")
    public String deleteFile(@RequestParam("fileName") String fileName) {
        s3FileManager.deleteObject(null, fileName);
        return REDIRECT;
    }

}
