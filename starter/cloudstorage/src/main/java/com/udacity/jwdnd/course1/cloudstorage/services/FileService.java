package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.File;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {
    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public File findFileByUserIdFilename(Integer userid, String filename) {
        return fileMapper.getFilesByUserIdFileName(userid, filename);
    }

    public List<File> findFilesByUserId(Integer userid) {
        return fileMapper.getFilesByUserId(userid);
    }

    public File findFileByFileId(Integer fileId) {
        return fileMapper.getFilesByFileId(fileId);
    }

    public int insert(File file) {
        return fileMapper.insert(file);
    }

    public int deleteByFileId(Integer fileId) {
        return fileMapper.deleteByFileId(fileId);
    }
}
