package com.original.officeweb.service.impl;

import com.original.officeweb.model.WebDocument;
import com.original.officeweb.service.WebView;
import com.original.officeweb.service.WebViewService;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Component
public class PDFWebView implements WebView {

    private final WebViewService webViewService;

    public PDFWebView(WebViewService webViewService) {
        this.webViewService = webViewService;
    }

    @Override
    public String handle(WebDocument document, Model model) throws IOException {
        String AttachGuid = UUID.randomUUID().toString();
        model.addAttribute("name", document.getName());
        model.addAttribute("pdfurl", "getFileContent?attachguid=" + AttachGuid);
        webViewService.setAttachCache(AttachGuid, document.getMd5());
        return "pdf";
    }

    @Override
    public Collection<String> support() {
        return Collections.singletonList("pdf");
    }
}
