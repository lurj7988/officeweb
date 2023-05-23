package com.original.officeweb.service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class WebViewFactory {

    private final Map<String, WebView> webViewCache;

    public WebViewFactory(Set<WebView> webViews) {
        this.webViewCache = new HashMap<>();
        for (WebView webView : webViews) {
            for (String suffix : webView.support()) {
                suffix = suffix.toLowerCase(Locale.ROOT);
                if (!webViewCache.containsKey(suffix)) {
                    webViewCache.put(suffix, webView);
                } else {
                    throw new RuntimeException(suffix + "有重复实现");
                }
            }
        }
    }

    public WebView getWebView(String suffix) {
        return webViewCache.get(suffix);
    }
}
