package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;


@Controller
@RequestMapping
public class CredentialController {
    private UserService userService;
    private CredentialService credentialService;
    private EncryptionService encryptionService;

    public CredentialController(UserService userService, CredentialService credentialService, EncryptionService encryptionService) {
        this.userService = userService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    @PostMapping("/credential/update")
    public String updateCredential(@ModelAttribute("credential") Credential credentialToUpdate, Authentication authentication) {
        String username = authentication.getName();
        Integer userId = userService.getUser(username).getUserId();
        credentialToUpdate.setUserId(userId);

        if (credentialToUpdate.getCredentialid() == null) {

            SecureRandom random = new SecureRandom();
            byte[] key = new byte[16];
            random.nextBytes(key);
            String encodedKey = Base64.getEncoder().encodeToString(key);

            credentialToUpdate.setKey(encodedKey);
            String password = credentialToUpdate.getPassword();
            credentialToUpdate.setPassword(encryptionService.encryptValue(password, credentialToUpdate.getKey()));
            credentialService.insert(credentialToUpdate);
        } else {
            Credential credential = credentialService.findCredentialByCredentialId(credentialToUpdate.getCredentialid());
            if (credential == null || userId != credential.getUserId()) return "redirect:/result?other";
            credentialToUpdate.setKey(credential.getKey());
            String password = credentialToUpdate.getPassword();
            credentialToUpdate.setPassword(encryptionService.encryptValue(password, credentialToUpdate.getKey()));
            credentialService.update(credentialToUpdate);
        }
        return "redirect:/result?success";
    }

    @GetMapping("/credential/delete")
    public String deleteNote(@RequestParam("credentialId") Integer credentialId, Authentication authentication) {
        String username = authentication.getName();
        Integer userId = userService.getUser(username).getUserId();
        //System.out.println(userId);

        //System.out.println(credentialId);
        Credential credential = credentialService.findCredentialByCredentialId(credentialId);
        //System.out.println(credential.getUserId());
        if (credential == null || userId != credential.getUserId()) return "redirect:/result?other";

        credentialService.deleteCredentialByCredentialId(credentialId);
        return "redirect:/result?success";

    }

}
