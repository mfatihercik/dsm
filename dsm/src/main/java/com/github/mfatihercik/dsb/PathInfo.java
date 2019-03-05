package com.github.mfatihercik.dsb;

public class PathInfo {
    private String tagName;
    private String parentPath;
    private String absolutePath;


    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public PathInfo set(String tagName, String parentPath, String absolutePath) {
        this.tagName = tagName;
        this.parentPath = parentPath;
        this.absolutePath = absolutePath;
        return this;
    }
}
