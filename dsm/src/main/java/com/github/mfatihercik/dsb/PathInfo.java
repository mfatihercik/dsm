package com.github.mfatihercik.dsb;

public class PathInfo {
    private String tagName;
    private String parentPath;
    private String absolutePath;


    public String getTagName() {
        return tagName;
    }

    public String getParentPath() {
        return parentPath;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }


    public PathInfo set(String tagName, String parentPath, String absolutePath) {
        this.tagName = tagName;
        this.parentPath = parentPath;
        this.absolutePath = absolutePath;
        return this;
    }
}
