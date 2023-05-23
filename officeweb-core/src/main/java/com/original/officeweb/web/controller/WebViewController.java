package com.original.officeweb.web.controller;

import com.epoint.boot.core.utils.string.StringUtils;
import com.original.officeweb.model.WebDocument;
import com.original.officeweb.service.WebView;
import com.original.officeweb.service.WebViewFactory;
import com.original.officeweb.service.WebViewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Controller
public class WebViewController {

    private final WebViewService webViewService;
    private final WebViewFactory webViewFactory;
    private final static Logger logger = LoggerFactory.getLogger(WebViewController.class);

    public WebViewController(WebViewService webViewService, WebViewFactory webViewFactory) {
        this.webViewService = webViewService;
        this.webViewFactory = webViewFactory;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String root(Model model, HttpServletRequest req) throws Exception {
        WebDocument document = webViewService.getWebDocument(req);
        WebView webView = webViewFactory.getWebView(document.getSuffix());
        if (webView == null) {
            throw new Exception("暂时不支持" + document.getSuffix() + "格式文件预览");
        } else {
            return webView.handle(document, model);
        }
    }

    @RequestMapping(value = "/getFileContent")
    public void getFileContent(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (StringUtils.isNotBlank(request.getParameter("attachguid"))) {
            String md5 = webViewService.getMD5ByAttachGuid(request.getParameter("attachguid"));
            WebDocument document = webViewService.getWebDocument(md5);
            if (document != null) {
                //如果有转换后的则取转换的
                download(Optional.ofNullable(document.getOutfilepath()).orElse(document.getFilepath()), request, response);
            } else {
                throw new IOException("文件获取异常");
            }
        }
    }

    public void download(String filePath, HttpServletRequest request, HttpServletResponse response) throws IOException {
        FileInputStream fis = null;
        OutputStream os = null;
        try {
            if (StringUtils.isBlank(filePath)) {
                throw new IOException("文件路径为空");
            }
            File file = new File(filePath);
            if (file.exists()) {
                // 设置响应类型，这里是下载pdf文件
                response.setContentType(filePath.substring(filePath.lastIndexOf(".") + 1));
                // 设置Content-Disposition，设置attachment，浏览器会激活文件下载框；filename指定下载后默认保存的文件名
                // 不设置Content-Disposition的话，文件会在浏览器内打卡，比如txt、img文件
                String userAgent = request.getHeader("User-Agent");
                String filename = file.getName();
                // 针对IE或者以IE为内核的浏览器：
                if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
                    filename = URLEncoder.encode(filename, "UTF-8");
                } else {
                    // 非IE浏览器的处理：
                    filename = new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
                }
                response.addHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", filename));
                response.setCharacterEncoding("UTF-8");
                os = response.getOutputStream();
                fis = new FileInputStream(file);
                // 缓冲区
                byte[] buffer = new byte[1024 * 4];
                // 记录读到缓冲buffer中的字节长度
                int ch;
                while ((ch = fis.read(buffer)) != -1) {
                    // 因为有可能出现ch与buffer的length不一致的问题,所以用下面的写法
                    os.write(buffer, 0, ch);
                }
                os.flush();
            } else {
                throw new IOException("文件不存在");
            }
        } catch (IOException e) {
            logger.error("文件读取失败：", e);
            throw new IOException("文件读取异常");
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                logger.error("文件流关闭失败：", e);
            }
        }
    }
}
