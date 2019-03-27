package com.github.mfatihercik.dsb.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

/**
 * Code taken from <a href="https://github.com/liquibase/liquibase">Liquivase</a>
 * Abstracts file access so they can be read in a variety of manners.
 */
public interface ResourceAccessor {

    /**
     * Return an InputStream for each resource mapped by the given path.
     * The path is often a URL but does not have to be.
     *
     * @return null if the resource does not exist.
     * @throws IOException if there is an error reading an existing path.
     */
    Set<InputStream> getResourcesAsStream(String path) throws IOException;

    ClassLoader toClassLoader();
}
