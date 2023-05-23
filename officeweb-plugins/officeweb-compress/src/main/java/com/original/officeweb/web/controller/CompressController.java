package com.original.officeweb.web.controller;

import com.epoint.boot.core.utils.security.crypto.MDUtils;
import com.epoint.boot.core.utils.string.StringUtils;
import com.original.officeweb.model.WebDocument;
import com.original.officeweb.service.WebView;
import com.original.officeweb.service.WebViewFactory;
import com.original.officeweb.service.WebViewService;
import com.original.officeweb.service.compress.CompressFactory;
import com.original.officeweb.service.compress.CompressService;
import com.original.officeweb.service.compress.LayuiTreeNode;
import com.original.officeweb.utils.LayuiTreeUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.InputStream;
import java.util.List;

import static com.original.officeweb.utils.WebUtils.suffixFromFileName;

@Controller
public class CompressController {

    private final CacheManager cacheManager;
    private final WebViewService webViewService;
    private final CompressFactory compressFactory;
    private final WebViewFactory webViewFactory;
    //private final static Logger logger = LoggerFactory.getLogger(CompressController.class);

    public CompressController(CacheManager cacheManager,
                              WebViewService webViewService,
                              CompressFactory compressFactory,
                              WebViewFactory webViewFactory) {
        this.cacheManager = cacheManager;
        this.webViewService = webViewService;
        this.compressFactory = compressFactory;
        this.webViewFactory = webViewFactory;
    }

    @RequestMapping(value = "/compress", method = RequestMethod.GET)
    public String root(Model model, HttpServletRequest req) throws Exception {
        WebDocument document = getWebDocument(req);
        WebView webView = webViewFactory.getWebView(document.getSuffix());
        if (webView == null) {
            throw new Exception("暂时不支持" + document.getSuffix() + "格式文件预览");
        } else {
            return webView.handle(document, model);
        }
    }

    public WebDocument getWebDocument(HttpServletRequest req) throws Exception {
        if (StringUtils.isNotBlank(req.getParameter("md5")) && StringUtils.isNotBlank(req.getParameter("id"))) {
            WebDocument document = webViewService.getWebDocument(req.getParameter("md5"));
            if (document != null && StringUtils.isNotBlank(document.getCompress())) {
                ObjectMapper objectMapper = new ObjectMapper();
                List<LayuiTreeNode> list = objectMapper.readValue(document.getCompress(),
                        objectMapper.getTypeFactory().constructParametricType(List.class, LayuiTreeNode.class));
                LayuiTreeNode treeNode = LayuiTreeUtils.scopeErgodic(list.get(0), req.getParameter("id"));
                if (treeNode != null) {
                    CompressService compressService = compressFactory.getCompressService(document.getSuffix());
                    InputStream is = compressService.getCompressChild(document.getFilepath(), treeNode.getHref());
                    if (is != null) {
                        return createWebDocument(is, treeNode.getTitle());
                    }
                }
            }
        }
        throw new Exception("压缩包内容获取失败！");
    }

    public WebDocument createWebDocument(InputStream is, String filename) throws Exception {
        WebDocument document = new WebDocument("");
        document.setName(filename);
        document.setMd5(MDUtils.md5Hex(is));
        document.setFiledirectory(webViewService.getFiledirectory(document));
        document.setFilepath(document.getFiledirectory() + File.separator + document.getName());
        document.setSuffix(suffixFromFileName(document.getName()));
        Cache cache = cacheManager.getCache("epoint.ehcache");
        if (cache != null) {
            File file = new File(document.getFilepath());
            WebDocument webDocument = cache.get(document.getMd5(), WebDocument.class);
            //如果缓存为空或者文件不存在
            if (webDocument == null || !file.exists() || !document.getFilepath().equals(webDocument.getFilepath())) {
                FileUtils.copyInputStreamToFile(is, file);
                cache.put(document.getMd5(), document);
                return document;
            } else {
                return webDocument;
            }
        } else {
            throw new Exception("缓存异常");
        }
    }

}
