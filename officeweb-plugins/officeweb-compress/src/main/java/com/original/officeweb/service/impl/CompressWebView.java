package com.original.officeweb.service.impl;

import com.original.officeweb.model.WebDocument;
import com.original.officeweb.service.WebView;
import com.original.officeweb.service.WebViewService;
import com.original.officeweb.service.compress.CompressFactory;
import com.original.officeweb.service.compress.CompressService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.Collection;
import java.util.Collections;

@Component
public class CompressWebView implements WebView {

    private final CompressFactory compressFactory;
    private final WebViewService webViewService;

    public CompressWebView(CompressFactory compressFactory, WebViewService webViewService) {
        this.compressFactory = compressFactory;
        this.webViewService = webViewService;
    }

    @Override
    public String handle(WebDocument document, Model model) throws Exception {
        CompressService compressService = compressFactory.getCompressService(document.getSuffix());
        if (compressService != null) {
            String fileTree = new ObjectMapper().writeValueAsString(Collections.singletonList(compressService.getCompressTreeNode(document)));
            document.setCompress(fileTree);
            webViewService.refreshCache(document);
            model.addAttribute("name", document.getName());
            model.addAttribute("filetree", fileTree);
            model.addAttribute("supports", String.join(",", compressFactory.getSupports()));
            model.addAttribute("md5", document.getMd5());
        }
        return "compress";
    }

    @Override
    public Collection<String> support() {
        return compressFactory.getSupports();
    }
}
