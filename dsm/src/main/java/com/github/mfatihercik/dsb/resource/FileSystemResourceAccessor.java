package com.github.mfatihercik.dsb.resource;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URI;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.GZIPInputStream;


/**
 * Code taken from <a href="https://github.com/liquibase/liquibase">Liquivase</a>
 * A @{link ResourceAccessor} implementation which finds Files in the File System.
 */
public class FileSystemResourceAccessor extends AbstractResourceAccessor {

    private final File baseDirectory;
    private boolean readyForInit;

    /**
     * Creates with no base directory. All files will be resolved exactly as they are given.
     */
    public FileSystemResourceAccessor() {
        baseDirectory = null;
        readyForInit = true;
        init();
    }


    /**
     * Creates with base directory for relative path support.
     * @param base base directory name
     */
    public FileSystemResourceAccessor(String base) {
        baseDirectory = new File(base);
        if (!baseDirectory.isDirectory()) {
            throw new IllegalArgumentException(base + " must be a directory");
        }
        readyForInit = true;
        init();
    }

    @Override
    protected void init() {
        if (readyForInit) {
            super.init();
        }
    }

    @Override
    protected void addRootPath(URL path) {
        try {
            URI uri = path.toURI();
            if(uri.isOpaque()) {
                return;
            }
            File pathAsFile = new File(uri);

            for (File fileSystemRoot : File.listRoots()) {
                if (pathAsFile.equals(fileSystemRoot)) { //don't include root
                    return;
                }
            }
        } catch (URISyntaxException e) {
            //add like normal
        }

        super.addRootPath(path);
    }

    @Override
    public Set<InputStream> getResourcesAsStream(String path) throws IOException {
        File absoluteFile = new File(path);
        File relativeFile = (baseDirectory == null) ? new File(path) : new File(baseDirectory, path);

        InputStream fileStream = null;
        if (absoluteFile.isAbsolute()) {
            try {
                fileStream = openStream(absoluteFile);
            } catch (FileNotFoundException e) {
                //will try relative
            }
        }

        if (fileStream == null) {
            try {
                fileStream = openStream(relativeFile);
            } catch (FileNotFoundException e2) {
                return null;
            }
        }


        Set<InputStream> returnSet = new HashSet<>();
        returnSet.add(fileStream);
        return returnSet;
    }

    private InputStream openStream(File file) throws IOException {
        if (file.getName().toLowerCase().endsWith(".gz")) {
            return new BufferedInputStream(new GZIPInputStream(new FileInputStream(file)));
        } else {
            return new BufferedInputStream(new FileInputStream(file));
        }
    }




    @Override
    protected String convertToPath(String string) {
        if (this.baseDirectory == null) {
            return string;
        } else {
            try {
                return "file:" + new File(string).getCanonicalPath().substring(this.baseDirectory.getCanonicalPath().length());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public ClassLoader toClassLoader() {
        try {
            URL url;
            if (baseDirectory == null) {
                url = new File("/").toURI().toURL();
            } else {
                url = baseDirectory.toURI().toURL();
            }
            return new URLClassLoader(new URL[]{url});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        File dir = baseDirectory;
        if (dir == null) {
            dir = new File(".");
        }
        return getClass().getName() + "(" + dir.getAbsolutePath() + ")";
    }

}
