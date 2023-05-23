package com.original.officeweb.config;

import com.original.officeweb.service.WebViewService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class ExcelWebConfig implements WebMvcConfigurer {
    private final WebViewService webViewService;

    public ExcelWebConfig(WebViewService webViewService) {
        this.webViewService = webViewService;
    }

    /**
     * 访问外部文件配置
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/*_files/*")
                .addResourceLocations("file:" + webViewService.getFileCache() + File.separator + "exceltemp" + File.separator);
    }
}
