package com.original.officeweb.service.compress;

import com.original.officeweb.model.WebDocument;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface CompressService {
    /**
     * 支持类型
     *
     * @return 支持类型集合
     */
    List<String> supports();

    /**
     * 获取压缩包内文件树形结构
     *
     * @param document 需要解压的文件属性
     * @return 压缩包内文件树形结构
     */
    LayuiTreeNode getCompressTreeNode(WebDocument document) throws IOException;
    

    InputStream getCompressChild(String compressPath, String childPath) throws IOException;
}
