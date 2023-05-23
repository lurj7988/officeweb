package com.original.officeweb.service.compress;

import com.original.officeweb.model.WebDocument;
import com.original.officeweb.utils.LayuiTreeUtils;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class CompressServiceSevenZImpl implements CompressService {

    @Override
    public List<String> supports() {
        return Collections.singletonList("7z");
    }

    @Override
    public LayuiTreeNode getCompressTreeNode(WebDocument document) throws IOException {
        List<LayuiTreeNode> list = LayuiTreeUtils.getFilePathTree(getSevenZPaths(document.getFilepath()), document.getMd5());
        return new LayuiTreeNode(document.getName(), document.getMd5(), "root", list, true);
    }

    @Override
    public InputStream getCompressChild(String compressPath, String childPath) throws IOException {
        SevenZFile zipFile = new SevenZFile(new File(compressPath));
        Iterable<SevenZArchiveEntry> entries = zipFile.getEntries();
        for (SevenZArchiveEntry entry : entries) {
            if (entry.getName().equals(childPath)) {
                return zipFile.getInputStream(entry);
            }
        }
        zipFile.close();
        return null;
    }

    public static List<String> getSevenZPaths(String SevenZPath) throws IOException {
        SevenZFile zipFile = new SevenZFile(new File(SevenZPath));
        Iterable<SevenZArchiveEntry> entries = zipFile.getEntries();
        List<String> paths = new ArrayList<>();
        for (SevenZArchiveEntry entry : entries) {
            paths.add(entry.getName());
        }
        zipFile.close();
        return paths;
    }
}
