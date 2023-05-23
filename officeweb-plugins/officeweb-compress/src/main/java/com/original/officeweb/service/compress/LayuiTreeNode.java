package com.original.officeweb.service.compress;

import java.util.List;

public class LayuiTreeNode {

    private String title;
    private String id;
    private String pid;
    private String field;
    private List<LayuiTreeNode> children;
    private String href;
    private boolean spread;
    private boolean checked;
    private boolean disabled;

    public LayuiTreeNode() {

    }

    public LayuiTreeNode(String title, String id, String pid, List<LayuiTreeNode> children, boolean spread) {
        this.title = title;
        this.id = id;
        this.pid = pid;
        this.children = children;
        this.spread = spread;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public List<LayuiTreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<LayuiTreeNode> children) {
        this.children = children;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public boolean isSpread() {
        return spread;
    }

    public void setSpread(boolean spread) {
        this.spread = spread;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
