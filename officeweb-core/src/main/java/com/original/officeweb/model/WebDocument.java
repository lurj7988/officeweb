package com.original.officeweb.model;

import java.io.Serializable;


public class WebDocument implements Serializable {
    private static final long serialVersionUID = 1L;

    public WebDocument(String url) {
        this.url = url;
    }

    private String suffix;
    private String name;
    private String md5;
    private String url;
    private String filepath;
    private String filedirectory;
    private String outfilepath;
    private String compress;

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getFiledirectory() {
        return filedirectory;
    }

    public void setFiledirectory(String filedirectory) {
        this.filedirectory = filedirectory;
    }

    public String getOutfilepath() {
        return outfilepath;
    }

    public void setOutfilepath(String outfilepath) {
        this.outfilepath = outfilepath;
    }

    public String getCompress() {
        return compress;
    }

    public void setCompress(String compress) {
        this.compress = compress;
    }
}
