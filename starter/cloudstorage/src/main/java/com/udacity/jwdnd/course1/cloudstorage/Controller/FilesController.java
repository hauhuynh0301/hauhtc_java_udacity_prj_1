package com.udacity.jwdnd.course1.cloudstorage.Controller;

import com.udacity.jwdnd.course1.cloudstorage.entity.ResponseFile;
import com.udacity.jwdnd.course1.cloudstorage.entity.SuperUser;
import com.udacity.jwdnd.course1.cloudstorage.services.FilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class FilesController {
    @Autowired
    private FilesService filesService;

    @PostMapping("/files")
    public String saveFile(Authentication authentication, MultipartFile fileUpload) throws Exception {
        SuperUser superUser = (SuperUser) authentication.getPrincipal();
        if (fileUpload.isEmpty()) {
            return "redirect:/result?error";
        }
        List<ResponseFile> listFile = filesService.getAllFiles(superUser.getUserid());
        if (listFile.size() > 0) {
            return "redirect:/result?error";
        }
        filesService.addFile(fileUpload, superUser.getUserid());
        return "redirect:/result?success";
    }

    @GetMapping("/files/delete")
    public String deleteNote(@RequestParam("id") int fileid) {
        if (fileid > 0) {
            filesService.deleteFile(fileid);
            return "redirect:/result?success";
        }
        return "redirect:/result?error";
    }
}
