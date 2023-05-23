package com.original.officeweb.service.impl;

import com.epoint.boot.core.utils.string.StringUtils;
import com.original.officeweb.model.WebDocument;
import com.original.officeweb.service.WebView;
import com.original.officeweb.service.WebViewService;
import com.original.officeweb.utils.AsposeSlidesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

@Component
public class PPTWebView implements WebView {

    private final WebViewService webViewService;
    private final static Logger logger = LoggerFactory.getLogger(PPTWebView.class);

    public PPTWebView(WebViewService webViewService) {
        this.webViewService = webViewService;
    }

    @Override
    public String handle(WebDocument document, Model model) throws Exception {
        if (StringUtils.isBlank(document.getOutfilepath())) {
            try {
                String fileName = document.getName();
                String pdfName = fileName.substring(0, fileName.lastIndexOf(".") + 1) + "pdf";
                String outFilePath = document.getFiledirectory() + File.separator + pdfName;
                document.setOutfilepath(outFilePath);
                AsposeSlidesUtils.ppt2pdf(document.getFilepath(), document.getOutfilepath());
                webViewService.refreshCache(document);
            } catch (Exception e) {
                logger.error("ppt转pdf失败", e);
                throw new Exception("ppt转pdf失败");
            }
        }
        String AttachGuid = UUID.randomUUID().toString();
        model.addAttribute("name", document.getName());
        model.addAttribute("pdfurl", "getFileContent?attachguid=" + AttachGuid);
        webViewService.setAttachCache(AttachGuid, document.getMd5());
        return "pdf";
    }

    @Override
    public Collection<String> support() {
        return Arrays.asList("ppt", "pptx");
    }
}
