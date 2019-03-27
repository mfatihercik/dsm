package com.github.mfatihercik.dsb.resource;

import com.github.mfatihercik.dsb.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;


public abstract class AbstractResourceAccessor implements ResourceAccessor {

    //We don't use an HashSet otherwise iteration order is not deterministic
    private final List<String> rootStrings = new ArrayList<>();
    private static final boolean IS_OS_WINDOWS = isOsWindows();


    protected AbstractResourceAccessor() {
        init();
    }

    protected void init() {
        try {
            Enumeration<URL> baseUrls;
            ClassLoader classLoader = toClassLoader();
            if (classLoader != null) {
                if (classLoader instanceof URLClassLoader) {
                    baseUrls = new Vector<>(Arrays.asList(((URLClassLoader) classLoader).getURLs())).elements();

                    while (baseUrls.hasMoreElements()) {
                        addRootPath(baseUrls.nextElement());
                    }
                }

                baseUrls = classLoader.getResources("");

                while (baseUrls.hasMoreElements()) {
                    addRootPath(baseUrls.nextElement());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getSystemProperty(String property) {
        try {
            return System.getProperty(property);
        } catch (SecurityException ex) {
            return null;
        }
    }

    protected void addRootPath(URL path) {
        if (path == null) {
            return;
        }
        String externalForm = path.toExternalForm();
        if (externalForm.startsWith("file:")) {
            try {
                externalForm = new File(path.toURI()).getCanonicalFile().toURI().toURL().toExternalForm();
            } catch (Exception e) {
                //keep original version
            }
        }
        if (!externalForm.endsWith("/")) {
            externalForm += "/";
        }
        if (!rootStrings.contains(externalForm)) {
            rootStrings.add(externalForm);
        }
    }

    protected List<String> getRootPaths() {
        return rootStrings;
    }


    protected String convertToPath(String string) {
        string = string.replace("\\", "/");

        String stringAsUrl = string;
        if (!stringAsUrl.matches("[a-zA-Z0-9]{2,}:.*")) {
            if (stringAsUrl.startsWith("/")) {
                stringAsUrl = "file:" + stringAsUrl;
            } else {
                stringAsUrl = "file:/" + stringAsUrl;
            }
        }
        for (String rootString : getRootPaths()) {
            boolean matches;
            if (isCaseSensitive()) {
                matches = stringAsUrl.startsWith(rootString);
            } else {
                matches = stringAsUrl.toLowerCase().startsWith(rootString.toLowerCase());
            }

            if (matches) {
                string = stringAsUrl.substring(rootString.length());
                break;
            }
        }

        string = string.replaceFirst("^//", "/");
        while (string.matches(".*[^:]//.*")) {
            string = string.replaceAll("([^:])//", "$1/");
        }
        while (string.contains("/./")) {
            string = string.replace("/./", "/");
        }
        while (string.matches(".*/.*?/\\.\\./.*")) {
            string = string.replaceAll("/[^/]+/\\.\\./", "/");
        }

        return string;
    }

    protected String convertToPath(String relativeTo, String path) {
        if (StringUtils.trimToNull(relativeTo) == null) {
            return path;
        }
        URL baseUrl = toClassLoader().getResource(relativeTo);
        if (baseUrl == null) {
            throw new RuntimeException("Cannot find base path '" + relativeTo + "'");
        }
        String base;
        if (baseUrl.toExternalForm().startsWith("file:")) {
            File baseFile = new File(baseUrl.getPath());
            if (!baseFile.exists()) {
                throw new RuntimeException("Base file '" + baseFile.getAbsolutePath() + "' does not exist");
            }
            if (baseFile.isFile()) {
                baseFile = baseFile.getParentFile();
            }
            base = baseFile.toURI().getPath();
        } else if (baseUrl.toExternalForm().startsWith("jar:file:")) {
            return convertToPath(new File(relativeTo).getParent() + '/' + path);
        } else {
            base = relativeTo;
        }
        String separator = "";
        if (!base.endsWith("/") && !path.startsWith("/")) {
            separator = "/";
        }
        if (base.endsWith("/") && path.startsWith("/")) {
            base = base.substring(0, base.length() - 1);
        }
        return convertToPath(base + separator + path);
    }

    private static boolean isOsWindows() {
        String OS_NAME = getSystemProperty("os.name");
        if (OS_NAME == null) {
            return false;
        }
        return OS_NAME.startsWith("Windows");
    }

    protected boolean isCaseSensitive() {
        return !IS_OS_WINDOWS;
    }



}
