package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entity.Files;
import com.udacity.jwdnd.course1.cloudstorage.entity.ResponseFile;
import com.udacity.jwdnd.course1.cloudstorage.repository.FilesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilesService {
    @Autowired
    private FilesRepository filesRepository;

    public ResponseFile getResponseFile(Files file) {
        String base64 = Base64.getEncoder().encodeToString(file.getFiledata());
        String dataURL = "data:" + file.getContenttype() + ";base64," + base64;
        return ResponseFile.builder().fileid(file.getFileid()).filename(file.getFilename()).dataURL(dataURL).build();
    }

    public List<ResponseFile> getAllFiles(int userid) throws Exception {
        List<Files> files = filesRepository.findByUserId(userid);
        if (files == null) {
            throw new Exception();
        }
        return files.stream().map(this::getResponseFile).collect(Collectors.toList());
    }

    public void addFile(MultipartFile fileUpload, int userid) throws IOException {
        Files file = new Files();
        try {
            file.setContenttype(fileUpload.getContentType());
            file.setFiledata(fileUpload.getBytes());
            file.setFilename(fileUpload.getOriginalFilename());
            file.setFilesize(Long.toString(fileUpload.getSize()));
        } catch (IOException e) {
            throw e;
        }
        filesRepository.insertFile(file, userid);
    }

    public void deleteFile(int fileid) {
        filesRepository.deleteFile(fileid);
    }
}
