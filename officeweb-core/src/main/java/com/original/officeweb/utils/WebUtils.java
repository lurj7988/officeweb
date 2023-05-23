package com.original.officeweb.utils;


import com.original.officeweb.model.FileDownloadInfo;
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.ContentDisposition;
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.ParseException;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class WebUtils {

    private final static Logger logger = LoggerFactory.getLogger(WebUtils.class);

    /**
     * 判断url是否是http资源
     *
     * @param url url
     * @return 是否http
     */
    public static boolean isHttpUrl(URL url) {
        //return url.getProtocol().toLowerCase().startsWith("file") || url.getProtocol().toLowerCase().startsWith("http");
        //file协议有安全漏洞
        return url.getProtocol().toLowerCase().startsWith("http");
    }

    public static boolean isHttpUrl(String url) {
        return url != null && url.toLowerCase().startsWith("http");
    }

    /**
     * 判断url是否是ftp资源
     *
     * @param url url
     * @return 是否ftp
     */
    public static boolean isFtpUrl(URL url) {
        return "ftp".equalsIgnoreCase(url.getProtocol());
    }

    /**
     * 获取url中的参数
     *
     * @param url  url
     * @param name 参数名
     * @return 参数值
     */
    public static String getUrlParameterReg(String url, String name) {
        Map<String, String> mapRequest = new HashMap<>();
        String strUrlParam = truncateUrlPage(url);
        if (strUrlParam == null) {
            return "";
        }
        //每个键值为一组
        String[] arrSplit = strUrlParam.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = strSplit.split("[=]");
            //解析出键值
            if (arrSplitEqual.length > 1) {
                //正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
            } else if (!arrSplitEqual[0].equals("")) {
                //只有参数没有值，不加入
                mapRequest.put(arrSplitEqual[0], "");
            }
        }
        return mapRequest.get(name);
    }


    /**
     * 去掉url中的路径，留下请求参数部分
     *
     * @param strURL url地址
     * @return url请求参数部分
     */
    private static String truncateUrlPage(String strURL) {
        String strAllParam = null;
        strURL = strURL.trim();
        String[] arrSplit = strURL.split("[?]");
        if (strURL.length() > 1) {
            if (arrSplit.length > 1) {
                if (arrSplit[1] != null) {
                    strAllParam = arrSplit[1];
                }
            }
        }
        return strAllParam;
    }

    /**
     * 从url中剥离出文件名
     *
     * @param url 格式如：http://www.com.cn/20171113164107_月度绩效表模板(新).xls?UCloudPublicKey=ucloudtangshd@weifenf.com14355492830001993909323&Expires=&Signature=I D1NOFtAJSPT16E6imv6JWuq0k=
     * @return 文件名
     */
    public static String getFileNameFromURL(String url) {
        // 因为url的参数中可能会存在/的情况，所以直接url.lastIndexOf("/")会有问题
        // 所以先从？处将url截断，然后运用url.lastIndexOf("/")获取文件名
        String noQueryUrl = url.substring(0, url.contains("?") ? url.indexOf("?") : url.length());
        noQueryUrl = noQueryUrl.substring(noQueryUrl.lastIndexOf("/") + 1);
        //如果不包含后缀直接返回空
        return noQueryUrl.contains(".") ? noQueryUrl : "";
    }


    /**
     * 从url中获取文件后缀
     *
     * @param url url
     * @return 文件后缀
     */
    public static String suffixFromUrl(String url) {
        String nonPramStr = url.substring(0, url.contains("?") ? url.indexOf("?") : url.length());
        String fileName = nonPramStr.substring(nonPramStr.lastIndexOf("/") + 1);
        return suffixFromFileName(fileName);
    }

    /**
     * 通过文件名获取文件后缀
     *
     * @param fileName 文件名称
     * @return 文件后缀
     */
    public static String suffixFromFileName(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    public static FileDownloadInfo getFileDownloadInfo(String url) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpRequestBase httpget = new HttpGet(url);
            CloseableHttpResponse response = httpclient.execute(httpget);
            Header[] headers = response.getHeaders("content-disposition");
            if (headers.length > 0) {
                String name = getWebNameFromHeader(headers[0]);
                return new FileDownloadInfo(URLDecoder.decode(name, StandardCharsets.UTF_8.name()),
                        IOUtils.toByteArray(response.getEntity().getContent()));
            }
        } catch (IOException e) {
            logger.error("下载失败", e);
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                logger.error("连接关闭失败", e);
            }
        }
        return null;
    }

    private static String getWebNameFromHeader(Header header) {
        try {
            ContentDisposition disposition = new ContentDisposition(header.getValue());
            String filename = disposition.getParameterList().get("filename");
            return new String(filename.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        } catch (ParseException e) {
            return "";
        }
    }

    public static byte[] callRestful(String server, InputStream in) throws IOException {
        OutputStream outputStream = null;
        byte[] content;
        try {
            int length = in.available();
            URL url = new URL(server);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setConnectTimeout(10000);
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            httpConn.setUseCaches(false);
            httpConn.setReadTimeout(Integer.MAX_VALUE);
            httpConn.setRequestMethod("POST");
            httpConn.setRequestProperty("Content-length", length + "");
            httpConn.setRequestProperty("Content-Type", "application/octet-stream");
            httpConn.setRequestProperty("Connection", "Keep-Alive");
            httpConn.setRequestProperty("Charset", "UTF-8");
            outputStream = httpConn.getOutputStream();
            byte[] buffer = new byte[4096];
            int ch;
            while ((ch = in.read(buffer)) != -1) {
                outputStream.write(buffer, 0, ch);
            }
            int responseCode = httpConn.getResponseCode();
            logger.info("responseCode:" + responseCode);
            if (200 == responseCode) {
                content = IOUtils.toByteArray(httpConn.getInputStream());
            } else {
                return null;
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
        return content;
    }

    /**
     * 在服务端获取Request对象
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        HttpServletRequest servletRequest = null;
        if (RequestContextHolder.getRequestAttributes() != null) {
            servletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        }
        return servletRequest;
    }

    public static String getBaseUrl() {
        HttpServletRequest request = getRequest();
//        return request.getScheme() + "://" + request.getServerName() + ":" +
//                request.getServerPort() + request.getContextPath() + "/";
        //https的情况会有问题
        return request.getRequestURL().toString();
    }
}
