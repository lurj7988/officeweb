package com.original.officeweb.utils;

import com.aspose.slides.FontsLoader;
import com.aspose.slides.License;
import com.aspose.slides.Presentation;
import com.aspose.slides.SaveFormat;
import com.epoint.boot.core.utils.classpath.ClassPathUtils;
import com.epoint.boot.core.utils.os.OSUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class AsposeSlidesUtils {

    public static void ppt2pdf(String inputFilePath, String outputFilePath) throws Exception {
        // 验证License
        if (!regLicense()) {
            throw new Exception("License认证失败");
        }
        //下面两行为引用外部的字体的代码，实验证明，源文件使用的字体和外部引用的字体要对应，不然转出来的pdf的字体会乱码，注意文件夹的位置mac和windows注意区分
        //font为引入的文件夹名称，根据自己所配置的文件夹进行调整
        if (OSUtils.isWindows()) {
            // 扫描用户字体目录
            String username = System.getProperties().getProperty("user.name");
            FontsLoader.loadExternalFonts(new String[]{ClassPathUtils.getDeployWarPath() + "fonts",
                    "C:/Windows/Fonts",
                    String.format("C:\\Users\\%s\\AppData\\Local\\Microsoft\\Windows\\Fonts", username)});//true为是否递归文件夹
        } else if (OSUtils.isMacOS()) {
            FontsLoader.loadExternalFonts(new String[]{ClassPathUtils.getDeployWarPath() + "fonts",
                    "/System/Library/Fonts"});
        } else if (OSUtils.isMacOSX()) {
            FontsLoader.loadExternalFonts(new String[]{ClassPathUtils.getDeployWarPath() + "fonts",
                    "/System/Library/Fonts"});
        } else if (OSUtils.isLinux()) {
            FontsLoader.loadExternalFonts(new String[]{ClassPathUtils.getDeployWarPath() + "fonts",
                    "/usr/share/fonts"});
        }
        Presentation pres = new Presentation(inputFilePath);//输入pdf路径
        try {
            pres.save(outputFilePath, SaveFormat.Pdf);
        } finally {
            pres.dispose();
            //清空字体缓存
            FontsLoader.clearCache();
        }
    }

    public static boolean regLicense() {
        try {
            String lic = "<License>\n" +
                    "    <Data>\n" +
                    "        <Products>\n" +
                    "            <Product>Aspose.Total for Java</Product>\n" +
                    "            <Product>Aspose.Words for Java</Product>\n" +
                    "        </Products>\n" +
                    "        <EditionType>Enterprise</EditionType>\n" +
                    "        <SubscriptionExpiry>20991231</SubscriptionExpiry>\n" +
                    "        <LicenseExpiry>20991231</LicenseExpiry>\n" +
                    "        <SerialNumber>8bfe198c-7f0c-4ef8-8ff0-acc3237bf0d7</SerialNumber>\n" +
                    "    </Data>\n" +
                    "    <Signature>sNLLKGMUdF0r8O1kKilWAGdgfs2BvJb/2Xp8p5iuDVfZXmhppo+d0Ran1P9TKdjV4ABwAgKXxJ3jcQTqE/2IRfqwnPf8itN8aFZlV3TJPYeD3yWE7IT55Gz6EijUpC7aKeoohTb4w2fpox58wWoF3SNp6sK6jDfiAUGEHYJ9pjU=</Signature>\n" +
                    "</License>";
            InputStream is = new ByteArrayInputStream(lic.getBytes(StandardCharsets.UTF_8));
            License aposeLic = new License();
            aposeLic.setLicense(is);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
