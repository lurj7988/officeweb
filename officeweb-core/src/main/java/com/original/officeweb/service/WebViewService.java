package com.original.officeweb.service;

import com.epoint.boot.core.utils.classpath.ClassPathUtils;
import com.epoint.boot.core.utils.security.crypto.MDUtils;
import com.epoint.boot.core.utils.string.StringUtils;
import com.original.officeweb.config.OfficeWebProperties;
import com.original.officeweb.decrypt.DecryptFactory;
import com.original.officeweb.decrypt.DecryptModel;
import com.original.officeweb.decrypt.DecryptService;
import com.original.officeweb.model.FileDownloadInfo;
import com.original.officeweb.model.WebDocument;
import com.original.officeweb.utils.WebUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class WebViewService {

    private final CacheManager cacheManager;
    private final DecryptFactory decryptFactory;
    private final OfficeWebProperties officeWebProperties;
    private final static Logger logger = LoggerFactory.getLogger(WebViewService.class);

    public WebViewService(CacheManager cacheManager, DecryptFactory decryptFactory, OfficeWebProperties officeWebProperties) {
        this.cacheManager = cacheManager;
        this.decryptFactory = decryptFactory;
        this.officeWebProperties = officeWebProperties;
    }

    public WebDocument getWebDocument(HttpServletRequest req) throws Exception {
        if (StringUtils.isBlank(req.getParameter("furl"))) {
            throw new Exception("缺少furl参数");
        }
        DecryptService decryptService = decryptFactory.getDecryptService(DecryptModel.to(req.getParameter("decryptmodel")));
        String furl = decryptService.decrypt(req.getParameter("furl"));
        logger.info(furl);
        WebDocument document = new WebDocument(furl);
        if (WebUtils.isHttpUrl(furl)) {
            FileDownloadInfo downloadInfo = WebUtils.getFileDownloadInfo(furl);
            byte[] data;
            if (downloadInfo != null) {
                document.setName(downloadInfo.getFilename());
                data = downloadInfo.getBytes();
            } else {
                //如果是直接访问路径下载而不是通过接口的获取不到FileDownloadInfo
                URL url = new URL(furl);
                document.setName(WebUtils.getFileNameFromURL(furl));
                data = IOUtils.toByteArray(url.openStream());
            }
            if (StringUtils.isNotBlank(req.getParameter("fname"))) {
                String fname = req.getParameter("fname");
                //TODO 这边要是测试一下到底用哪个
                fname = URLDecoder.decode(fname, StandardCharsets.UTF_8.name());
                fname = new String(fname.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                if (isMessyCode(fname)) {
                    fname = UUID.randomUUID().toString() + "." + WebUtils.suffixFromFileName(fname);
                }
                document.setName(fname);
            } else if (StringUtils.isBlank(document.getName())) {
                throw new Exception("缺少fname参数");
            }
            if (data.length > officeWebProperties.getFile().getLimitsize() * 1024L * 1024L) {
                throw new Exception("文件超过最大预览限制" + officeWebProperties.getFile().getLimitsize() + "MB");
            }
            document.setMd5(MDUtils.md5Hex(new ByteArrayInputStream(data)));
            document.setFiledirectory(getFiledirectory(document));
            document.setFilepath(document.getFiledirectory() + File.separator + document.getName());
            document.setSuffix(WebUtils.suffixFromFileName(document.getName()));
            Cache cache = cacheManager.getCache("epoint.ehcache");
            if (cache != null) {
                File file = new File(document.getFilepath());
                WebDocument webDocument = cache.get(document.getMd5(), WebDocument.class);
                //如果缓存为空或者文件不存在
                if (webDocument == null || !file.exists() || !document.getFilepath().equals(webDocument.getFilepath())) {
                    FileUtils.writeByteArrayToFile(file, data);
                    cache.put(document.getMd5(), document);
                    return document;
                } else {
                    return webDocument;
                }
            } else {
                throw new Exception("缓存异常");
            }
        } else {
            throw new Exception("目前只支持http或https协议");
        }
    }

    public void refreshCache(WebDocument document) {
        Cache cache = cacheManager.getCache("epoint.ehcache");
        if (cache != null) {
            cache.put(document.getMd5(), document);
        }
    }

    public String getFileCache() {
        //如果是绝对路径
        if (officeWebProperties.getFile().getCache().startsWith("/")) {
            return officeWebProperties.getFile().getCache();
        }
        return ClassPathUtils.getDeployWarPath() + File.separator + officeWebProperties.getFile().getCache();
    }

    public WebDocument getWebDocument(String md5) {
        Cache cache = cacheManager.getCache("epoint.ehcache");
        if (cache != null) {
            return cache.get(md5, WebDocument.class);
        }
        return null;
    }

    public String getMD5ByAttachGuid(String AttachGuid) {
        Cache cache = cacheManager.getCache("epoint.ehcache");
        if (cache != null) {
            String md5 = cache.get(AttachGuid, String.class);
            cache.evict(AttachGuid);
            return md5;
        }
        return null;
    }

    public void setAttachCache(String key, String md5) {
        Cache cache = cacheManager.getCache("epoint.ehcache");
        if (cache != null) {
            cache.put(key, md5);
        }
    }

    public String getFiledirectory(WebDocument document) {
        String fileCache = getFileCache();
        File dirFile = new File(fileCache);
        if (!dirFile.exists() && !dirFile.mkdirs()) {
            logger.error("创建目录【{}】失败,可能是权限不够，请检查", fileCache);
        }
        File Rel = new File(fileCache + File.separator + document.getMd5());
        if (!Rel.exists() && !Rel.mkdirs()) {
            logger.error("创建目录【{}】失败,可能是权限不够，请检查", fileCache + File.separator + document.getMd5());
        }
        return Rel.getAbsolutePath();
    }

    public static boolean isMessyCode(String strName) {
        Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");
        Matcher m = p.matcher(strName);
        String after = m.replaceAll("");
        String temp = after.replaceAll("\\p{P}", "");
        char[] ch = temp.trim().toCharArray();
        float chLength = 0;
        float count = 0;
        for (char c : ch) {
            if (!Character.isLetterOrDigit(c)) {
                if (!isChinese(c)) {
                    count = count + 1;
                }
                chLength++;
            }
        }
        float result = count / chLength;
        return result > 0.4;
    }

    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS ||
                ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS ||
                ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A ||
                ub == Character.UnicodeBlock.GENERAL_PUNCTUATION ||
                ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION ||
                ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }
}
