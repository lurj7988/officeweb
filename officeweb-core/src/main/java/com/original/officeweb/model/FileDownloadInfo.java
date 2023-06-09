package com.original.officeweb.model;

import java.io.Serializable;

public class FileDownloadInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String filename;

    private byte[] bytes;

    public FileDownloadInfo(String filename, byte[] bytes) {
        this.filename = filename;
        this.bytes = bytes;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
