package com.original.officeweb.utils;


import com.original.officeweb.service.compress.LayuiTreeNode;

import java.util.*;
import java.util.stream.Collectors;

public class LayuiTreeUtils {

    /**
     * 深度遍历
     *
     * @param root    根节点
     * @param childid 查询节点
     * @param list    查询结果
     */
    public static void depthErgodic(LayuiTreeNode root, String childid, List<LayuiTreeNode> list) {
        if (root == null) return;
        if (childid.equals(root.getId())) {
            list.add(root);
        }
        for (LayuiTreeNode node : root.getChildren()) {
            depthErgodic(node, childid, list);
        }
    }

    /**
     * 广度遍历查询
     *
     * @param root    根节点
     * @param childid 查询节点
     */
    public static LayuiTreeNode scopeErgodic(LayuiTreeNode root, String childid) {
        Queue<LayuiTreeNode> queue = new LinkedList<>();
        queue.add(root);
        LayuiTreeNode tree;
        tree = queue.poll();
        while (null != tree) {
            if (childid.equals(tree.getId())) {
                return tree;
            }
            for (LayuiTreeNode tr : tree.getChildren()) {
                queue.offer(tr);
            }
            tree = queue.poll();
        }
        return null;
    }

    /**
     * 递归查询子节点
     *
     * @param root 根节点
     * @param all  所有节点
     * @return 根节点信息
     */
    private static List<LayuiTreeNode> getChildrens(LayuiTreeNode root, List<LayuiTreeNode> all) {
        return all.stream().filter(m -> Objects.equals(m.getPid(), root.getId())).peek((m) -> {
            List<LayuiTreeNode> list = getChildrens(m, all);
            m.setSpread(!list.isEmpty());
            m.setChildren(list);
        }).collect(Collectors.toList());
    }

    private static Map<String, String> getFilePathMap(List<String> paths) {
        Map<String, String> map = new LinkedHashMap<>();
        for (String s : paths) {
            String[] path = s.split("/");
            StringBuilder p = new StringBuilder();
            for (String value : path) {
                p.append(value).append("/");
                if (!map.containsKey(p.substring(0, p.length() - 1))) {
                    map.put(p.substring(0, p.length() - 1), UUID.randomUUID().toString());
                }
            }
        }
        return map;
    }

    /**
     * @param paths 文件路径集合
     * @param root  根节点id
     * @return 树形结构
     */
    public static List<LayuiTreeNode> getFilePathTree(List<String> paths, String root) {
        Map<String, String> map = getFilePathMap(paths);
        List<LayuiTreeNode> layuiTreeNodes = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            LayuiTreeNode layuiTreeNode = new LayuiTreeNode();
            String values = entry.getValue();
            String[] keys = entry.getKey().split("/");
            layuiTreeNode.setId(values);
            if (keys.length == 1) {
                layuiTreeNode.setPid(root);
                layuiTreeNode.setTitle(keys[0]);
                layuiTreeNode.setHref(keys[0]);
            } else {
                StringBuilder path = new StringBuilder();
                for (int i = 0; i < keys.length - 1; i++) {
                    path.append(keys[i]).append("/");
                }
                layuiTreeNode.setTitle(keys[keys.length - 1]);
                layuiTreeNode.setHref(String.join("/", keys));
                path = new StringBuilder(path.substring(0, path.length() - 1));
                layuiTreeNode.setPid(map.get(path.toString()));
            }
            layuiTreeNodes.add(layuiTreeNode);
        }
        //获取父节点
        return layuiTreeNodes.stream().filter(m -> m.getPid().equals(root)).peek(
                m -> {
                    List<LayuiTreeNode> list = getChildrens(m, layuiTreeNodes);
                    m.setSpread(!list.isEmpty());
                    m.setChildren(list);
                }
        ).collect(Collectors.toList());
    }
}
