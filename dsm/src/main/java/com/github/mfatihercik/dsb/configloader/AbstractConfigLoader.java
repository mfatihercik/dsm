package com.github.mfatihercik.dsb.configloader;

import com.github.mfatihercik.dsb.ConfigLoaderStrategy;
import com.github.mfatihercik.dsb.resource.ClassLoaderResourceAccessor;
import com.github.mfatihercik.dsb.resource.CompositeResourceAccessor;
import com.github.mfatihercik.dsb.resource.FileSystemResourceAccessor;
import com.github.mfatihercik.dsb.resource.ResourceAccessor;
import com.github.mfatihercik.dsb.utils.MapUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractConfigLoader implements ConfigLoaderStrategy {

    private InputStream inputStream;
    private ResourceAccessor resourceAccessor = null;
    private String rootPath = null;

    public AbstractConfigLoader(String configContent, String rootDirectory) {
        this(stringToInputStream(configContent), rootDirectory);
    }

    public AbstractConfigLoader(InputStream inputStream, String rootDirectory) {
        setInputStream(inputStream);
        setResourceAccessor(rootDirectory);
    }

    private static ByteArrayInputStream stringToInputStream(String configContent) {
        return new ByteArrayInputStream(configContent.getBytes(Charset.forName("UTF-8")));
    }

    @Override
    public Map<String, Object> readConfiguration() {
        return readConfiguration(inputStream);
    }

    public Map<String, Object> readConfiguration(String content) {
        return readConfiguration(stringToInputStream(content));
    }

    @Override
    public Map<String, Object> readExtendConfiguration(String filePath) {
        try {
            Set<InputStream> resourcesAsStream = resourceAccessor.getResourcesAsStream(filePath);
            Map<String, Object> configuration = null;
            for (InputStream stream : resourcesAsStream) {
                if (configuration == null) {
                    configuration = readConfiguration(stream);
                } else {
                    MapUtils.mergeMap(configuration, readConfiguration(stream));
                }
            }
            return configuration;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public ResourceAccessor getResourceAccessor() {
        return resourceAccessor;
    }

    private void setResourceAccessor(String rootDirectory) {
        List<ResourceAccessor> resourceAccessors = new ArrayList<>();
        resourceAccessors.add(new FileSystemResourceAccessor());
        resourceAccessors.add(new ClassLoaderResourceAccessor(this.getClass().getClassLoader()));
        String changeLogDirectory = rootDirectory;
        if (changeLogDirectory != null) {
            changeLogDirectory = changeLogDirectory.trim().replace('\\', '/'); // convert to standard / if using absolute path on windows
            resourceAccessors.add(new FileSystemResourceAccessor(changeLogDirectory));
        }
        setResourceAccessor(new CompositeResourceAccessor(resourceAccessors));
    }

    public void setResourceAccessor(ResourceAccessor resourceAccessor) {
        this.resourceAccessor = resourceAccessor;
    }

}
