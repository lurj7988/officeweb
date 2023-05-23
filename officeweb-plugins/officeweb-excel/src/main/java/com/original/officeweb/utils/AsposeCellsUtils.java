package com.original.officeweb.utils;

import com.aspose.cells.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class AsposeCellsUtils {

    public static void excel2html(String inputFilePath, String outputFilePath) throws Exception {
        regLicense();
        LoadOptions loadOption = new TxtLoadOptions(LoadFormat.AUTO);
        Workbook workbook = new Workbook(inputFilePath, loadOption);
        HtmlSaveOptions options = new HtmlSaveOptions();
        //options.setAddTooltipText(true);
        //options.setPresentationPreference(true);
        workbook.save(outputFilePath, options);
    }

    public static void regLicense() {
        String lic = "<?xml version=\"1.0\"?>\n" +
                "<License>\n" +
                "  <Data>\n" +
                "    <LicensedTo>Guotai Epoint Software Co.,Ltd</LicensedTo>\n" +
                "    <EmailTo>1002583770@qq.com</EmailTo>\n" +
                "    <LicenseType>Developer OEM</LicenseType>\n" +
                "    <LicenseNote>1 Developer And Unlimited Deployment Locations</LicenseNote>\n" +
                "    <OrderID>210429065838</OrderID>\n" +
                "    <UserID>881344</UserID>\n" +
                "    <OEM>This is a redistributable license</OEM>\n" +
                "    <Products>\n" +
                "      <Product>Aspose.Cells for Java</Product>\n" +
                "    </Products>\n" +
                "    <EditionType>Professional</EditionType>\n" +
                "    <SerialNumber>188a6b17-bd47-470d-b385-7871b756b6c8</SerialNumber>\n" +
                "    <SubscriptionExpiry>20220429</SubscriptionExpiry>\n" +
                "    <LicenseVersion>3.0</LicenseVersion>\n" +
                "    <LicenseInstructions>https://purchase.aspose.com/policies/use-license</LicenseInstructions>\n" +
                "  </Data>\n" +
                "  <Signature>MViHaoQ4Lv+38AaMIOT3lsUEYMIN/mEEV2/BTPHoguct2Z5h7kkSsxb9/aoPOAKFhdv+gA0tGsuvEoz/jVfEQJnxDEv/f2edRIeiO8aIfldWCQEVEJe8VoY+XUGVD+wWSbnGgPEmM9HLqsz3D8bdIJssfx5Z0Z0yhHhA1dcRXNo=</Signature>\n" +
                "</License>";
        InputStream is = new ByteArrayInputStream(lic.getBytes(StandardCharsets.UTF_8));
        License asposeLic = new License();
        asposeLic.setLicense(is);
    }
}
