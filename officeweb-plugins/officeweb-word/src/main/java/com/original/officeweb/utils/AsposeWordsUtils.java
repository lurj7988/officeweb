package com.epoint.officeweb.utils;

import com.aspose.words.*;
import com.epoint.boot.core.utils.classpath.ClassPathUtils;
import com.epoint.boot.core.utils.os.OSUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class AsposeWordsUtils {

    public static void word2pdf(String inputFilePath, String outputFilePath) throws Exception {
        regLicense();
        //font为引入的文件夹名称，根据自己所配置的文件夹进行调整
        FontSettings fontSettings = new FontSettings();
        if (OSUtils.isWindows()) {
            // 扫描用户字体目录
            String username = System.getProperties().getProperty("user.name");
            fontSettings.setFontsFolders(new String[]{ClassPathUtils.getDeployWarPath() + "fonts",
                    "C:/Windows/Fonts",
                    String.format("C:\\Users\\%s\\AppData\\Local\\Microsoft\\Windows\\Fonts", username)}, true);//true为是否递归文件夹
        } else if (OSUtils.isMacOS()) {
            fontSettings.setFontsFolders(new String[]{ClassPathUtils.getDeployWarPath() + "fonts",
                    "/System/Library/Fonts"}, true);
        } else if (OSUtils.isMacOSX()) {
            fontSettings.setFontsFolders(new String[]{ClassPathUtils.getDeployWarPath() + "fonts",
                    "/System/Library/Fonts"}, true);
        } else if (OSUtils.isLinux()) {
            fontSettings.setFontsFolders(new String[]{ClassPathUtils.getDeployWarPath() + "fonts",
                    "/usr/share/fonts"}, true);
        }
        LoadOptions loadOptions = new LoadOptions();
        loadOptions.setFontSettings(fontSettings);
        Document doc = new Document(inputFilePath, loadOptions);
        doc.save(outputFilePath, SaveFormat.PDF);
    }

    public static void regLicense() throws Exception {
        String lic = "<License>\n" +
                "  <Data>\n" +
                "    <LicensedTo>Jangsu Guotai Epoint Software CO.,LTD</LicensedTo>\n" +
                "    <EmailTo>chb@epoint.com.cn</EmailTo>\n" +
                "    <LicenseType>Developer OEM</LicenseType>\n" +
                "    <LicenseNote>Limited to 1 developer, unlimited physical locations</LicenseNote>\n" +
                "    <OrderID>200525225553</OrderID>\n" +
                "    <UserID>135014654</UserID>\n" +
                "    <OEM>This is a redistributable license</OEM>\n" +
                "    <Products>\n" +
                "      <Product>Aspose.Words Product Family</Product>\n" +
                "    </Products>\n" +
                "    <EditionType>Enterprise</EditionType>\n" +
                "    <SerialNumber>2deace04-d9bc-48c4-bdf9-6a7cf773b207</SerialNumber>\n" +
                "    <SubscriptionExpiry>20210525</SubscriptionExpiry>\n" +
                "    <LicenseVersion>3.0</LicenseVersion>\n" +
                "    <LicenseInstructions>https://purchase.aspose.com/policies/use-license</LicenseInstructions>\n" +
                "  </Data>\n" +
                "  <Signature>HXbzc3hrjesaB0LF5i8UC0gnuGianJHe4UzJZhVlfHqhxutn8Zr+Fwk69DxqWT827ESgzFnxKZQ8n2bRE7SWqeorMyRQ0FXJMRc1mjpf8sbX2gaFyicBNa5NDwxonlLkPBrgbP6qON12c12IVIfthTXg/1M6HFHUfZGcBk7nq7s=</Signature>\n" +
                "</License>";
        InputStream is = new ByteArrayInputStream(lic.getBytes(StandardCharsets.UTF_8));
        License aposeLic = new License();
        aposeLic.setLicense(is);
    }
}
