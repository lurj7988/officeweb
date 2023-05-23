package com.original.officeweb.service.impl;

import com.epoint.boot.core.utils.string.StringUtils;
import com.original.officeweb.model.WebDocument;
import com.original.officeweb.service.WebView;
import com.original.officeweb.service.WebViewService;
import com.original.officeweb.utils.AsposeCellsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;

@Component
public class ExcelWebView implements WebView {

    private final WebViewService webViewService;
    private final static Logger logger = LoggerFactory.getLogger(ExcelWebView.class);

    public ExcelWebView(WebViewService webViewService) {
        this.webViewService = webViewService;
    }

    @Override
    public String handle(WebDocument document, Model model) throws Exception {
        if (StringUtils.isBlank(document.getOutfilepath())) {
            try {
                String outFilePath = webViewService.getFileCache() + File.separator + "exceltemp" + File.separator + document.getMd5() + ".html";
                document.setOutfilepath(outFilePath);
                AsposeCellsUtils.excel2html(document.getFilepath(), document.getOutfilepath());
                webViewService.refreshCache(document);
            } catch (Exception e) {
                logger.error("excel转html失败", e);
                throw new Exception("excel转html失败");
            }
        }
        try {
            byte[] bytes = FileCopyUtils.copyToByteArray(new FileInputStream(document.getOutfilepath()));
            model.addAttribute("excelhtml", new String(bytes, StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("excelhtml", "显示失败");
        }
        return "excel";
    }

    @Override
    public Collection<String> support() {
        return Arrays.asList("xls", "xlsx");
    }
}
