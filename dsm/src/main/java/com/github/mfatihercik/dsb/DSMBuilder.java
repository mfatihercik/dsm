package com.github.mfatihercik.dsb;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mfatihercik.dsb.configloader.ConfigFormat;
import com.github.mfatihercik.dsb.configloader.FileParsingElementLoader;
import com.github.mfatihercik.dsb.configloader.JsonConfigLoaderStrategy;
import com.github.mfatihercik.dsb.configloader.YamlConfigLoaderStrategy;
import com.github.mfatihercik.dsb.expression.ExpressionResolver;
import com.github.mfatihercik.dsb.expression.ExpressionResolverFactory;
import com.github.mfatihercik.dsb.function.DefaultFunctionFactory;
import com.github.mfatihercik.dsb.function.FunctionContext;
import com.github.mfatihercik.dsb.function.FunctionExecutor;
import com.github.mfatihercik.dsb.function.FunctionFactory;
import com.github.mfatihercik.dsb.json.JacksonStreamParser;
import com.github.mfatihercik.dsb.transformation.FileValueTransformer;
import com.github.mfatihercik.dsb.transformation.ValueTransformer;
import com.github.mfatihercik.dsb.typeadapter.TypeAdaptor;
import com.github.mfatihercik.dsb.typeadapter.TypeAdaptorFactory;
import com.github.mfatihercik.dsb.typeconverter.TypeConverter;
import com.github.mfatihercik.dsb.typeconverter.TypeConverterFactory;
import com.github.mfatihercik.dsb.xml.StaxParser;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class DSMBuilder {

    private ExpressionResolver expressionResolver = null;
    private ConfigLoaderStrategy configLoaderStrategy = null;
    private ValueTransformer valueTransformer = new FileValueTransformer();
    private FunctionFactory functionFactory = new DefaultFunctionFactory(new FunctionContext());
    private TypeAdaptorFactory typeAdaptorFactory = new TypeAdaptorFactory();
    private TypeConverterFactory typeConverterFactory = new TypeConverterFactory();
    private TYPE type = TYPE.JSON;
    private ObjectMapper objectMapper = new ObjectMapper();
    private InputStream configContent = null;
    private String rootPath;
    private ConfigFormat configFormat = ConfigFormat.YAML;

    private Map<String, Object> extParamsMap = new HashMap<>();

    public Map<String, Object> getParams() {
        return extParamsMap;
    }

    public DSMBuilder setParams(Map<String, Object> params) {
        if (extParamsMap == null)
            throw new IllegalArgumentException("params can not be null");
        this.extParamsMap.putAll(extParamsMap);
        return this;
    }

    private String scriptingLang = "jexl3";

    {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public DSMBuilder(String configContent) {
        this(configContent, null);
    }

    public DSMBuilder(String configContent, String rootDirectory) {
        this(new ByteArrayInputStream(configContent.getBytes(Charset.forName("UTF-8"))), rootDirectory);
    }

    public DSMBuilder(InputStream inputStream) {
        this(inputStream, null);
    }

    public DSMBuilder(InputStream inputStream, String rootDirectory) {
        this.configContent = inputStream;
        this.rootPath = rootDirectory;
    }

    public DSMBuilder(File configFile) throws IOException {
        this(configFile, configFile.getParentFile().getAbsolutePath());

    }

    public DSMBuilder(File configFile, String rootDirectory) throws IOException {
        this(new FileInputStream(configFile), rootDirectory);
    }

    public DSMBuilder(ConfigLoaderStrategy configLoaderStrategy) {
        this.configLoaderStrategy = configLoaderStrategy;
    }

    public DSM create() {
        return create(null);
    }

    public DSM create(Class<?> resultType) {
        if (configLoaderStrategy == null) {
            if (ConfigFormat.YAML == configFormat) {
                configLoaderStrategy = new YamlConfigLoaderStrategy(configContent, rootPath);
            } else {
                configLoaderStrategy = new JsonConfigLoaderStrategy(configContent, rootPath);
            }
        }
        if (expressionResolver == null) {
            expressionResolver = ExpressionResolverFactory.getExpressionResolver(scriptingLang);
        }
        StreamParser parser = type.equals(TYPE.JSON) ? new JacksonStreamParser(functionFactory, expressionResolver, objectMapper, resultType, getTypeConverterFactory().clone()) : new StaxParser(functionFactory, expressionResolver, objectMapper, resultType, getTypeConverterFactory().clone());
        FileParsingElementLoader configLoader = new FileParsingElementLoader(configLoaderStrategy, expressionResolver, valueTransformer, functionFactory.getContext(), getTypeAdaptorFactory().clone(), extParamsMap);
        return new DSM(parser, configLoader, objectMapper);
    }

    /**
     * register new function with given name.
     *
     * @param functionName     name of function to register
     * @param functionExecutor {@link FunctionExecutor} instance to register
     * @return DSMBuilder
     */
    public DSMBuilder registerFunction(String functionName, FunctionExecutor functionExecutor) {
        this.getFunctionFactory().getContext().registerFunction(functionName, functionExecutor);
        return this;
    }

    /**
     * Register new type adapter with name.
     *
     * @param name         name of the type adapter to register
     * @param adapterClass {@link Class} of the {@link TypeAdaptor}
     * @return self
     */
    public DSMBuilder registerTypeAdapter(String name, Class<? extends TypeAdaptor> adapterClass) {
        this.getTypeAdaptorFactory().register(name, adapterClass);
        return this;
    }

    /**
     * Register new type converter with name
     *
     * @param name          name of {@link TypeConverter} to register. name can be referenced "dataType" field in DSM document
     * @param typeConverter {@link TypeConverter} instance
     * @return self
     */
    public DSMBuilder registerTypeConverter(String name, TypeConverter typeConverter) {
        this.getTypeConverterFactory().register(name, typeConverter);
        return this;
    }

    public DSMBuilder removeFunction(String functionName) {
        this.getFunctionFactory().getContext().remove(functionName);
        return this;
    }

    public DSMBuilder getRegisteredFunction(String functionName) {
        this.getFunctionFactory().getContext().get(functionName);
        return this;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public DSMBuilder setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        return this;
    }

    public InputStream getConfigContent() {
        return configContent;
    }

    public DSMBuilder setConfigContent(InputStream configContent) {
        this.configContent = configContent;
        return this;
    }

    public String getRootPath() {
        return rootPath;
    }

    public DSMBuilder setRootPath(String rootPath) {
        this.rootPath = rootPath;
        return this;
    }

    public ConfigFormat getConfigFormat() {
        return configFormat;
    }

    public DSMBuilder setConfigFormat(ConfigFormat configFormat) {
        this.configFormat = configFormat;
        return this;
    }

    public FunctionFactory getFunctionFactory() {
        return functionFactory;
    }

    public DSMBuilder setFunctionFactory(FunctionFactory functionFactory) {
        this.functionFactory = functionFactory;
        return this;
    }

    public ExpressionResolver getExpressionResolver() {
        return expressionResolver;
    }

    public DSMBuilder setExpressionResolver(ExpressionResolver expressionResolver) {
        this.expressionResolver = expressionResolver;
        return this;
    }

    public ConfigLoaderStrategy getConfigLoaderStrategy() {
        return configLoaderStrategy;
    }

    public DSMBuilder setConfigLoaderStrategy(ConfigLoaderStrategy configLoaderStrategy) {
        this.configLoaderStrategy = configLoaderStrategy;
        return this;
    }

    public ValueTransformer getValueTransformer() {
        return valueTransformer;
    }

    public DSMBuilder setValueTransformer(ValueTransformer valueTransformer) {
        this.valueTransformer = valueTransformer;
        return this;
    }

    public TypeAdaptorFactory getTypeAdaptorFactory() {
        return typeAdaptorFactory;
    }

    public DSMBuilder setTypeAdaptorFactory(TypeAdaptorFactory typeAdaptorFactory) {
        this.typeAdaptorFactory = typeAdaptorFactory;
        return this;
    }

    public TypeConverterFactory getTypeConverterFactory() {
        return typeConverterFactory;
    }

    public DSMBuilder setTypeConverterFactory(TypeConverterFactory typeConverterFactory) {
        this.typeConverterFactory = typeConverterFactory;
        return this;
    }

    public String getScriptingLang() {
        return scriptingLang;
    }

    public DSMBuilder setScriptingLang(String scriptingLang) {
        this.scriptingLang = scriptingLang;
        this.setExpressionResolver(null);
        return this;
    }

    public TYPE getType() {
        return type;
    }

    public DSMBuilder setType(TYPE type) {
        this.type = type;
        return this;
    }

    public enum TYPE {
        JSON, XML
    }

}
