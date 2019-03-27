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

public class DCMBuilder {

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

    private String scriptingLang = "jexl3";

    {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public DCMBuilder(String configContent) {
        this(configContent, null);
    }

    public DCMBuilder(String configContent, String rootDirectory) {
        this(new ByteArrayInputStream(configContent.getBytes(Charset.forName("UTF-8"))), rootDirectory);
    }

    public DCMBuilder(InputStream inputStream) {
        this(inputStream, null);
    }

    public DCMBuilder(InputStream inputStream, String rootDirectory) {
        this.configContent = inputStream;
        this.rootPath = rootDirectory;
    }

    public DCMBuilder(File configFile) throws IOException {
        this(configFile, configFile.getParentFile().getAbsolutePath());

    }

    public DCMBuilder(File configFile, String rootDirectory) throws IOException {
        this(new FileInputStream(configFile), rootDirectory);
    }

    public DCMBuilder(ConfigLoaderStrategy configLoaderStrategy) {
        this.configLoaderStrategy = configLoaderStrategy;
    }

    public DCM create() {
        return create(null);
    }

    public DCM create(Class<?> resultType) {
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
        FileParsingElementLoader configLoader = new FileParsingElementLoader(configLoaderStrategy, expressionResolver, valueTransformer, functionFactory.getContext(), getTypeAdaptorFactory().clone());
        return new DCM(parser, configLoader, objectMapper);
    }

    /**
     * register new function with given name.
     *
     * @param functionName
     * @param functionExecutor
     * @return
     */
    public DCMBuilder registerFunction(String functionName, FunctionExecutor functionExecutor) {
        this.getFunctionFactory().getContext().registerFunction(functionName, functionExecutor);
        return this;
    }

    /**
     * Register new type adapter with name.
     *
     * @param name
     * @param adapterClass
     * @return
     */
    public DCMBuilder registerTypeAdapter(String name, Class<? extends TypeAdaptor> adapterClass) {
        this.getTypeAdaptorFactory().register(name, adapterClass);
        return this;
    }

    /**
     * Register new type converter with name
     *
     * @param name
     * @param typeConverter
     * @return
     */
    public DCMBuilder registerTypeConverter(String name, TypeConverter typeConverter) {
        this.getTypeConverterFactory().register(name, typeConverter);
        return this;
    }

    public DCMBuilder removeFunction(String functionName) {
        this.getFunctionFactory().getContext().remove(functionName);
        return this;
    }

    public DCMBuilder getRegisteredFunction(String functionName) {
        this.getFunctionFactory().getContext().get(functionName);
        return this;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public DCMBuilder setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        return this;
    }

    public InputStream getConfigContent() {
        return configContent;
    }

    public DCMBuilder setConfigContent(InputStream configContent) {
        this.configContent = configContent;
        return this;
    }

    public String getRootPath() {
        return rootPath;
    }

    public DCMBuilder setRootPath(String rootPath) {
        this.rootPath = rootPath;
        return this;
    }

    public ConfigFormat getConfigFormat() {
        return configFormat;
    }

    public DCMBuilder setConfigFormat(ConfigFormat configFormat) {
        this.configFormat = configFormat;
        return this;
    }

    public FunctionFactory getFunctionFactory() {
        return functionFactory;
    }

    public DCMBuilder setFunctionFactory(FunctionFactory functionFactory) {
        this.functionFactory = functionFactory;
        return this;
    }

    public ExpressionResolver getExpressionResolver() {
        return expressionResolver;
    }

    public DCMBuilder setExpressionResolver(ExpressionResolver expressionResolver) {
        this.expressionResolver = expressionResolver;
        return this;
    }

    public ConfigLoaderStrategy getConfigLoaderStrategy() {
        return configLoaderStrategy;
    }

    public DCMBuilder setConfigLoaderStrategy(ConfigLoaderStrategy configLoaderStrategy) {
        this.configLoaderStrategy = configLoaderStrategy;
        return this;
    }

    public ValueTransformer getValueTransformer() {
        return valueTransformer;
    }

    public DCMBuilder setValueTransformer(ValueTransformer valueTransformer) {
        this.valueTransformer = valueTransformer;
        return this;
    }

    public TypeAdaptorFactory getTypeAdaptorFactory() {
        return typeAdaptorFactory;
    }

    public DCMBuilder setTypeAdaptorFactory(TypeAdaptorFactory typeAdaptorFactory) {
        this.typeAdaptorFactory = typeAdaptorFactory;
        return this;
    }

    public TypeConverterFactory getTypeConverterFactory() {
        return typeConverterFactory;
    }

    public DCMBuilder setTypeConverterFactory(TypeConverterFactory typeConverterFactory) {
        this.typeConverterFactory = typeConverterFactory;
        return this;
    }

    public String getScriptingLang() {
        return scriptingLang;
    }

    public DCMBuilder setScriptingLang(String scriptingLang) {
        this.scriptingLang = scriptingLang;
        this.setExpressionResolver(null);
        return this;
    }

    public TYPE getType() {
        return type;
    }

    public DCMBuilder setType(TYPE type) {
        this.type = type;
        return this;
    }

    public enum TYPE {
        JSON, XML
    }

}
