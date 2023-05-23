package com.original.officeweb.config;

import com.original.officeweb.decrypt.DecryptFactory;
import com.original.officeweb.decrypt.DecryptService;
import com.original.officeweb.service.WebView;
import com.original.officeweb.service.WebViewFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Set;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 访问外部文件配置
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
    }

    @Bean
    @ConditionalOnMissingBean
    public WebViewFactory webViewFactory(Set<WebView> webViews) {
        return new WebViewFactory(webViews);
    }

    @Bean
    @ConditionalOnMissingBean
    public DecryptFactory decryptFactory(Set<DecryptService> decryptServices) {
        return new DecryptFactory(decryptServices);
    }
}
