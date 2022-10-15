package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.File;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class FileController {

    private UserService userService;
    private FileService fileService;

    public FileController(UserService userService, FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
    }

    @PostMapping("/file/upload")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile fileUpload, Authentication authentication) throws IOException {

        if (fileUpload.isEmpty()) return "redirect:/result?error";

        String username = authentication.getName();
        Integer userId = userService.getUser(username).getUserId();
        String filename = fileUpload.getOriginalFilename();
        File file = fileService.findFileByUserIdFilename(userId, filename);
        if (file != null) return "redirect:/result?other";

        file = new File();
        file.setContenttype(fileUpload.getContentType());
        file.setFiledata(fileUpload.getBytes());
        file.setFilename(filename);
        file.setFilesize(String.valueOf(fileUpload.getSize()));
        file.setUserid(userId);

        int result = fileService.insert(file);
        if (result > 0) return "redirect:/result?success";
        else return "redirect:/result?error";

    }

    @GetMapping("/file/download")
    public ResponseEntity downloadFile(@RequestParam("fileId") Integer fileId, Authentication authentication) {

        String username = authentication.getName();
        Integer userId = userService.getUser(username).getUserId();
        File file = fileService.findFileByFileId(fileId);

        if (file == null || userId != file.getUserid()) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContenttype()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(new ByteArrayResource(file.getFiledata()));
    }

    @GetMapping("/file/delete")
    public String deleteFile(@RequestParam("fileId") Integer fileId, Authentication authentication) {
        String username = authentication.getName();
        Integer userId = userService.getUser(username).getUserId();
        File file = fileService.findFileByFileId(fileId);

        if (file == null || userId != file.getUserid()) return "redirect:/result?other";
        else {
            fileService.deleteByFileId(fileId);
            return "redirect:/result?success";
        }
    }

}
