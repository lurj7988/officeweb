package com.original.officeweb.service.impl;

import com.original.officeweb.model.WebDocument;
import com.original.officeweb.service.WebView;
import com.original.officeweb.service.WebViewService;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Component
public class OFDWebView implements WebView {

    private final WebViewService webViewService;

    public OFDWebView(WebViewService webViewService) {
        this.webViewService = webViewService;
    }

    @Override
    public String handle(WebDocument document, Model model) {
        String AttachGuid = UUID.randomUUID().toString();
        model.addAttribute("name", document.getName());
        model.addAttribute("ofdurl", "getFileContent?attachguid=" + AttachGuid);
        webViewService.setAttachCache(AttachGuid, document.getMd5());
        return "ofd";
    }

    @Override
    public Collection<String> support() {
        return Collections.singletonList("ofd");
    }
}
