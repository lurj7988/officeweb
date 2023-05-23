package com.original.officeweb.service.compress;

import com.original.officeweb.model.WebDocument;
import com.original.officeweb.utils.LayuiTreeUtils;
import info.monitorenter.cpdetector.CharsetPrinter;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

@Component
public class CompressServiceZipImpl implements CompressService {

    @Override
    public List<String> supports() {
        return Arrays.asList("zip", "jar", "gzip");
    }

    @Override
    public LayuiTreeNode getCompressTreeNode(WebDocument document) throws IOException {
        List<LayuiTreeNode> list = LayuiTreeUtils.getFilePathTree(getZipPaths(document.getFilepath()), document.getMd5());
        return new LayuiTreeNode(document.getName(), document.getMd5(), "root", list, true);
    }

    @Override
    public InputStream getCompressChild(String compressPath, String childPath) throws IOException {
        ZipFile zipFile = new ZipFile(compressPath, getFileEncode(new File(compressPath)));
        Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();
        while (entries.hasMoreElements()) {
            ZipArchiveEntry entry = entries.nextElement();
            if (entry.getName().equals(childPath)) {
                return zipFile.getInputStream(entry);
            }
        }
        zipFile.close();
        return null;
    }

    public List<String> getZipPaths(String ZipPath) throws IOException {
        ZipFile zipFile = new ZipFile(ZipPath, getFileEncode(new File(ZipPath)));
        Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();
        List<String> paths = new ArrayList<>();
        while (entries.hasMoreElements()) {
            ZipArchiveEntry entry = entries.nextElement();
            paths.add(entry.getName());
        }
        zipFile.close();
        return paths;
    }

    /**
     * 检测文件编码格式
     *
     * @param file 检测的文件
     * @return 编码格式
     */
    public String getFileEncode(File file) {
        CharsetPrinter cp = new CharsetPrinter();
        try {
            String encoding = cp.guessEncoding(file);
            // 解决commons-compress解压报错问题，同时解压出来的文件中文不为乱码
            if ("UTF-16LE".equals(encoding) || "UTF-16BE".equals(encoding)) {
                return "GBK";
            }
            return encoding;
        } catch (IOException e) {
            return "UTF-8";
        }
    }
}
