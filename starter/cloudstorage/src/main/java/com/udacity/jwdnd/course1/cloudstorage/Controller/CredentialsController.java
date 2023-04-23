package com.udacity.jwdnd.course1.cloudstorage.Controller;

import com.udacity.jwdnd.course1.cloudstorage.entity.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.entity.SuperUser;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CredentialsController {
    @Autowired
    private CredentialsService credentialsService;

    @PostMapping("/credential")
    public String createOrUpdateCredential(Authentication authentication, Credentials credential) {
        SuperUser superUser = (SuperUser) authentication.getPrincipal();
        int checkCreateOrUpdate = credential.getCredentialid() == 0 ? credentialsService.addCredential(credential, superUser.getUserid()) : credentialsService.updateCredential(credential);
        if (checkCreateOrUpdate == 1) {
            return "redirect:/result?success";
        }
        return "redirect:/result?error";
    }

    @GetMapping("/credential/delete")
    public String deleteNote(@RequestParam("credentialId") int credentialId) {
        if (credentialId > 0) {
            int result = credentialsService.deleteCredential(credentialId);
            if (result == 1){
                return "redirect:/result?success";
            }
        }
        return "redirect:/result?error";
    }
}
