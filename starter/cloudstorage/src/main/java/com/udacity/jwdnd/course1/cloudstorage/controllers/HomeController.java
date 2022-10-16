package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class HomeController {

    private UserService userService;
    private FileService fileService;
    private NoteService noteService;
    private CredentialService credentialService;
    private EncryptionService encryptionService;

    public HomeController(UserService userService, FileService fileService, NoteService noteService, CredentialService credentialService, EncryptionService encryptionService) {
        this.userService = userService;
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    @GetMapping("/home")
    public String getHomePage(Authentication auth, Model model) {
        User user = userService.getUser(auth.getName());
        if (user == null) return "redirect:/login";

        // Needed by th:object="${note}" in home.html
        model.addAttribute("note", new Note());
        model.addAttribute("credential", new Credential());
        model.addAttribute("encryptionService", this.encryptionService);

        model.addAttribute("files", fileService.findFilesByUserId(user.getUserId()));
        model.addAttribute("notes", noteService.findNotesByUserId(user.getUserId()));
        model.addAttribute("credentials", credentialService.findCredentialsByUserId(user.getUserId()));

        return "home";
    }

    @GetMapping("")
    public String hello(Authentication auth, Model model) {
        User user = userService.getUser(auth.getName());
        if (user == null) return "redirect:/login";
        return "home";
    }

}

