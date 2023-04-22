package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entity.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.repository.CredentialsRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CredentialsService {
    @Autowired
    private CredentialsRepository credentialsRepository;

    @Autowired
    private EncryptionService encryptionService;

    private Credentials encryptPassword(Credentials credential) {
        String key = RandomStringUtils.random(16, true, true);
        credential.setKey(key);
        credential.setPassword(encryptionService.encryptValue(credential.getPassword(), key));
        return credential;
    }

    public Credentials decryptPassword(Credentials credential) {
        credential.setPassword(encryptionService.decryptValue(credential.getPassword(), credential.getKey()));
        return credential;
    }

    public List<Credentials> getAllCredentials(int userid) throws Exception {
        List<Credentials> credentials = credentialsRepository.findByUserId(userid);
        if (credentials == null) {
            throw new Exception("List credentials not found");
        }
        return credentials.stream().map(this::decryptPassword).collect(Collectors.toList());
    }

    public void addCredential(Credentials credential, int userid) {
        credentialsRepository.insertCredentials(encryptPassword(credential), userid);
    }

    public void updateCredential(Credentials credential) {
        credentialsRepository.updateCredentials(encryptPassword(credential));
    }

    public void deleteCredential(int credentialid) {
        credentialsRepository.deleteCredentials(credentialid);
    }
}
