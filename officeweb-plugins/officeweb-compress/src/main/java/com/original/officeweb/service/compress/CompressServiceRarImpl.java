package com.original.officeweb.service.compress;

import com.original.officeweb.model.WebDocument;
import com.original.officeweb.utils.LayuiTreeUtils;
import net.sf.sevenzipjbinding.*;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import net.sf.sevenzipjbinding.simple.ISimpleInArchive;
import net.sf.sevenzipjbinding.simple.ISimpleInArchiveItem;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class CompressServiceRarImpl implements CompressService {

    @Override
    public List<String> supports() {
        return Collections.singletonList("rar");
    }

    @Override
    public LayuiTreeNode getCompressTreeNode(WebDocument document) {
        List<LayuiTreeNode> list = LayuiTreeUtils.getFilePathTree(getRarPaths(document.getFilepath()), document.getMd5());
        return new LayuiTreeNode(document.getName(), document.getMd5(), "root", list, true);
    }


    @Override
    public InputStream getCompressChild(String compressPath, String childPath) {
        RandomAccessFile randomAccessFile = null;
        IInArchive archive = null;
        try {
            // f -  压缩文件
            File f = new File(compressPath);
            randomAccessFile = new RandomAccessFile(f.getAbsolutePath(), "r");
            RandomAccessFileInStream t = new RandomAccessFileInStream(randomAccessFile);
            try {
                archive = SevenZip.openInArchive(ArchiveFormat.RAR5, t);
            } catch (SevenZipException e) {
                archive = SevenZip.openInArchive(ArchiveFormat.RAR, t);
            }
            ISimpleInArchive simpleInArchive = archive.getSimpleInterface();
            for (final ISimpleInArchiveItem item : simpleInArchive.getArchiveItems()) {
                if (item.getPath().replace("\\", "/").equals(childPath)) {
                    ChildFileOutStream childFileOutStream = new ChildFileOutStream();
                    //会分片解压
                    item.extractSlow(childFileOutStream);
                    return new ByteArrayInputStream(childFileOutStream.getData());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (archive != null) {
                try {
                    archive.close();
                } catch (SevenZipException e) {
                    e.printStackTrace();
                }
            }
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public List<String> getRarPaths(String RarPath) {
        RandomAccessFile randomAccessFile = null;
        IInArchive inArchive = null;
        List<String> paths = new ArrayList<>();
        try {
            randomAccessFile = new RandomAccessFile(RarPath, "r");
            RandomAccessFileInStream t = new RandomAccessFileInStream(randomAccessFile);
            try {
                inArchive = SevenZip.openInArchive(ArchiveFormat.RAR5, t);
            } catch (SevenZipException e) {
                inArchive = SevenZip.openInArchive(ArchiveFormat.RAR, t);
            }
            ISimpleInArchive simpleInArchive = inArchive.getSimpleInterface();
            for (final ISimpleInArchiveItem item : simpleInArchive.getArchiveItems()) {
                paths.add(item.getPath().replace("\\", "/"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inArchive != null) {
                try {
                    inArchive.close();
                } catch (SevenZipException e) {
                    e.printStackTrace();
                }
            }
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return paths;
    }

    private static class ChildFileOutStream implements ISequentialOutStream {

        private final List<byte[]> data = new ArrayList<>();

        @Override
        public int write(byte[] bytes) {
            data.add(bytes);
            return bytes.length;
        }

        public byte[] getData() throws IOException {
            ByteArrayOutputStream resultOutputStream = new ByteArrayOutputStream();
            for (byte[] item : data) {
                resultOutputStream.write(item);
            }
            return resultOutputStream.toByteArray();
        }

    }
}
