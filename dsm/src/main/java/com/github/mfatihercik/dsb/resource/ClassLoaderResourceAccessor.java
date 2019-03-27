package com.github.mfatihercik.dsb.resource;

import com.github.mfatihercik.dsb.utils.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.*;


/**
 * Code taken from <a href="https://github.com/liquibase/liquibase">Liquibase</a>
 * An implementation of {@link ResourceAccessor} that wraps a class loader.
 */
@SuppressWarnings("ALL")
public class ClassLoaderResourceAccessor extends AbstractResourceAccessor {

    private final ClassLoader classLoader;

    public ClassLoaderResourceAccessor() {
        this.classLoader = getClass().getClassLoader();
        init(); //init needs to be called after classloader is set
    }

    public ClassLoaderResourceAccessor(ClassLoader classLoader) {
        this.classLoader = classLoader;
        init(); //init needs to be called after classloader is set
    }

    @Override
    public Set<InputStream> getResourcesAsStream(String path) throws IOException {
        Enumeration<URL> resources = classLoader.getResources(path);
        if ((resources == null) || !resources.hasMoreElements()) {
            return null;
        }
        Set<String> seenUrls = new HashSet<>();
        Set<InputStream> returnSet = new HashSet<>();
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            if (seenUrls.contains(url.toExternalForm())) {
                continue;
            }
            seenUrls.add(url.toExternalForm());


            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            InputStream resourceAsStream = connection.getInputStream();
            if (resourceAsStream != null) {
                returnSet.add(resourceAsStream);
            }
        }

        return returnSet;
    }


    @Override
    public ClassLoader toClassLoader() {
        return classLoader;
    }

    @Override
    public String toString() {
        String description;
        if (classLoader instanceof URLClassLoader) {
            List<String> urls = new ArrayList<>();
            for (URL url : ((URLClassLoader) classLoader).getURLs()) {
                urls.add(url.toExternalForm());
            }
            description = StringUtils.join(urls, ",");
        } else {
            description = classLoader.getClass().getName();
        }
        return getClass().getName() + "(" + description + ")";

    }
}
