package com.udacity.jwdnd.course1.cloudstorage.entity;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Files {
    private int fileid;
    private String filename;
    private String contenttype;
    private String filesize;
    private byte[] filedata;
}
