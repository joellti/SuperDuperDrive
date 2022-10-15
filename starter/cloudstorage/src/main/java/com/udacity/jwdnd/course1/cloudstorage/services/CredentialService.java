package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {

    private CredentialMapper credentialMapper;

    public CredentialService(CredentialMapper credentialMapper) {
        this.credentialMapper = credentialMapper;
    }

    public int insert(Credential credential) {
        System.out.println("userId: " + credential.getUserId());
        return this.credentialMapper.insert(credential);
    }

    public int update(Credential credential) {
        return credentialMapper.update(credential);
    }

    public List<Credential> findCredentialsByUserId(Integer userId) {
        List<Credential> credentials = this.credentialMapper.findByUserId(userId);
        System.out.println("credentials.size() " + credentials.size());
        return credentials;
    }

    public Credential findCredentialByCredentialId(Integer id) {
        return this.credentialMapper.findByCredentialId(id);
    }

    public int deleteCredentialByCredentialId(Integer id) {
        return this.credentialMapper.delete(id);
    }
}
