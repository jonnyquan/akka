package com.utils;

import java.io.InputStream;

/**
 * Created by ruancl@xkeshi.com on 2017/1/11.
 */
public class FileEntry {
    private String ext;
    private InputStream inputStream;

    public FileEntry(String ext, InputStream inputStream) {
        this.ext = ext;
        this.inputStream = inputStream;
    }

    public String getExt() {
        return ext;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}
