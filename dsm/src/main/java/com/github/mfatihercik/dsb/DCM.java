package com.github.mfatihercik.dsb;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.URL;
import java.util.List;

public class DCM {
    private final StreamParser parser;
    private final ConfigLoader configLoader;
    private ObjectMapper objectMapper;

    DCM(StreamParser parser, ConfigLoader configLoader, ObjectMapper objectMapper) {
        super();
        this.parser = parser;
        this.configLoader = configLoader;
        this.setObjectMapper(objectMapper);
    }

    public DCM(StreamParser parser, ConfigLoader configLoader) {
        this(parser, configLoader, new ObjectMapper());
    }

    public void reload() {
        load(true);
    }

    public void load() {
        load(false);
    }

    protected void load(boolean reload) {
        if (reload || !configLoader.isLoaded()) {

            List<ParsingElement> load = configLoader.load(reload);

            parser.getFunctionFactory().setContext(configLoader.getFunctionContext());

            parser.setValueTransformer(configLoader.getValueTransformer());

            parser.setAllParsingElement(load);
            parser.setParams(configLoader.getParams());
            parser.init();
        }
    }

    protected void init() {
        load();
    }

    public Object toObject(File src) throws IOException {
        init();
        return parser.parse(new FileInputStream(src));
    }

    public Object toObject(URL src) throws IOException {
        init();
        return parser.parse(optimizedStreamFromURL(src));
    }

    public Object toObject(String content) {
        init();
        return parser.parse(new StringReader(content));
    }

    public Object toObject(InputStream src) {
        init();
        return parser.parse(src);
    }

    protected InputStream optimizedStreamFromURL(URL url) throws IOException {
        if ("file".equals(url.getProtocol())) {
            /*
             * Can not do this if the path refers to a network drive on windows. This fixes
             * the problem; might not be needed on all platforms (NFS?), but should not
             * matter a lot: performance penalty of extra wrapping is more relevant when
             * accessing local file system.
             */
            String host = url.getHost();
            if (host == null || host.length() == 0) {
                // [core#48]: Let's try to avoid probs with URL encoded stuff
                String path = url.getPath();
                if (path.indexOf('%') < 0) {
                    return new FileInputStream(url.getPath());

                }
                // otherwise, let's fall through and let URL decoder do its magic
            }
        }
        return url.openStream();
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

}
