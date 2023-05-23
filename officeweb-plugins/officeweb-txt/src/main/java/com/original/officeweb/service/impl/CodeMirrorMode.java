package com.original.officeweb.service.impl;

public class CodeMirrorMode {

    public CodeMirrorMode(String source, String mode) {
        this.source = source;
        this.mode = mode;
    }

    private String source;
    private String mode;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
