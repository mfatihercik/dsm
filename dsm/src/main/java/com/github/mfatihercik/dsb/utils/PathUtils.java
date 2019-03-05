package com.github.mfatihercik.dsb.utils;

import com.github.mfatihercik.dsb.configloader.ConfigConstants;

public class PathUtils {

    public static final String DOUBLE_DOT = "..";
    public static final String DOUBLE_SLASH = ConfigConstants.PATH_SEPARATOR.concat("{2,}");
    public static final String TRIPLE_DOT = "(\\.\\.) {3,}";

    public static String removeDuplicateSlash(String path) {
        return path.replaceAll(DOUBLE_SLASH, ConfigConstants.PATH_SEPARATOR);
    }

    public static String normalizeRelativePath(String path) {
        return path.replaceAll(DOUBLE_SLASH, ConfigConstants.PATH_SEPARATOR).replaceAll(TRIPLE_DOT, DOUBLE_DOT);
    }

    public static String removeRelativePath(String path) {
        return path.replaceAll("\\.", "").replaceAll(TRIPLE_DOT, DOUBLE_DOT);
    }
}
