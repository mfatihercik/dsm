package com.github.mfatihercik.dsb.configloader;

import com.github.mfatihercik.dsb.ConfigLoader;
import com.github.mfatihercik.dsb.ConfigLoaderStrategy;
import com.github.mfatihercik.dsb.ParsingElement;
import com.github.mfatihercik.dsb.expression.ExpressionResolver;
import com.github.mfatihercik.dsb.expression.ExpressionResolverFactory;
import com.github.mfatihercik.dsb.function.FunctionContext;
import com.github.mfatihercik.dsb.transformation.FileValueTransformer;
import com.github.mfatihercik.dsb.transformation.TransformationElement;
import com.github.mfatihercik.dsb.transformation.ValueTransformer;
import com.github.mfatihercik.dsb.typeadapter.TypeAdaptor;
import com.github.mfatihercik.dsb.utils.MapUtils;
import com.github.mfatihercik.dsb.utils.MapWrapper;

import java.util.*;
import java.util.Map.Entry;

public class FileParsingElementLoader implements ConfigLoader, ConfigConstants {

    private static final String XML = "xml";
    final ConfigLoaderStrategy configLoader;
    private final FunctionContext functionContext;
    private final ValueTransformer valueTransformer;
    private Map<String, Object> params = new HashMap<>();
    private Map<String, Object> definitions = new HashMap<>();
    private List<ParsingElement> parsingElements = new ArrayList<>();
    private ExpressionResolver expressionResolver;
    private boolean isLoaded = false;

    public FileParsingElementLoader(ConfigLoaderStrategy configLoader) {
        this(configLoader, ExpressionResolverFactory.getExpressionResolver(null), new FileValueTransformer(), new FunctionContext());
    }

    public FileParsingElementLoader(ConfigLoaderStrategy configLoader, ExpressionResolver expressionResolver, ValueTransformer valueTransformer, FunctionContext functionContext) {
        this.expressionResolver = expressionResolver;
        this.configLoader = configLoader;
        this.valueTransformer = valueTransformer;
        this.functionContext = functionContext;

        this.getExpressionResolver().setBean(PARAMS, getParams());
        this.getExpressionResolver().setBean(DEFINITIONS, definitions);
    }


    public List<ParsingElement> load(boolean reload) {
        if (isLoaded() && !reload) {
            return getParsingElements();
        }
        Map<String, Object> map = configLoader.readConfiguration();
        fillParameters(map);
        extendToExternalConfig(map);
        MapWrapper mainMap = new MapWrapper(map, null);


        mainMap.getField(RESULT, true);
        fillParameters(map);
        fillFunctions(map);
        fillDefinitions(map);
        fillTransformation(map);
        fillRoot(map);

        isLoaded = true;
        return getParsingElements();
    }

    @SuppressWarnings("unchecked")
    private void fillDefinitions(Map<String, Object> map) {
        if (map.containsKey(DEFINITIONS)) {
            MapUtils.mergeMap(getDefinitions(), (Map<String, Object>) map.get(DEFINITIONS));
        }
    }

    private void extendToExternalConfig(Map<String, Object> map) {
        if (map.containsKey(EXTENDS)) {
            String value = map.get(EXTENDS).toString().trim();
            if (value.startsWith(EXPRESSION_INDICATOR)) {
                value = getExpressionResolver().resolveExpression(value).toString();
            }
            Map<String, Object> importMap = configLoader.readExtendConfiguration(value);
            MapUtils.mergeMap(map, importMap);
        }
    }

    @SuppressWarnings("unchecked")
    private void fillParameters(Map<String, Object> map) {
        if (map.containsKey(PARAMS)) {
            Object value = map.get(PARAMS);
            boolean isInstanceOfMap = value instanceof Map<?, ?>;
            if (isInstanceOfMap) {
                getParams().putAll((Map<String, Object>) value);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void fillFunctions(Map<String, Object> map) {
        if (map.containsKey(FUNCTIONS)) {
            Object value = map.get(FUNCTIONS);
            boolean isInstanceOfMap = value instanceof Map<?, ?>;
            if (isInstanceOfMap) {
                functionContext.merge((Map<String, String>) value);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void fillTransformation(Map<String, Object> map) {
        if (map.containsKey(TRANSFORMATIONS)) {
            Object value = map.get(TRANSFORMATIONS);
            boolean isInstanceOfMap = value instanceof Map<?, ?>;
            if (isInstanceOfMap) {
                fillTransformations((Map<String, Object>) value);
            }
        }
    }


    private void fillRoot(Map<String, Object> map) {
        if (map.containsKey(RESULT)) {
            Object value = map.get(RESULT);
            boolean isInstanceOfMap = value instanceof Map<?, ?>;
            if (isInstanceOfMap) {
                buildParsingElementTree(value, RESULT, null, 0);
            }
        }
    }

    private void buildParsingElementTree(Object value, String fieldName, ParsingElement parent, int order) {
        @SuppressWarnings("unchecked")
        Map<String, Object> valueMap = (Map<String, Object>) value;
        extendToExternalConfig(valueMap);
        extendsToDefinitions(valueMap);
        MapWrapper elementMap = new MapWrapper(value, parent == null ? RESULT : parent.getFieldName());
        ParsingElement element = new ParsingElement();
        element.setFieldName(fieldName);
        if (parent != null)
            element.setParentElement(parent);
        element.setOrder(element.isRoot() ? order : Integer.valueOf(element.getParentElement().getOrder() + "" + order));
        setTagType(elementMap, element);
        setType(elementMap, element);
        element.setServiceName(elementMap.toString(FUNCTION));
        element.setUseFunction(elementMap.isExist(FUNCTION));
        element.setTagPath(elementMap.toString(TAG_PATH, parent == null));
        element.setTagParentPath(elementMap.toString(TAG_PARENT_PATH));
        element.setDefaultValue(elementMap.toString(DEFAULT));
        element.setDefault(elementMap.isExist(DEFAULT));
        element.setTransformEnabled(elementMap.isExist(TRANSFORMATION_CODE));
        element.setTransformationCode(elementMap.toString(TRANSFORMATION_CODE));
        element.setFilterExist(elementMap.isExist(FILTER));
        element.setFilterExpression(elementMap.toString(FILTER));
        element.setUniqueKey(elementMap.toString(UNIQUE_KEY, fieldName));
        element.setTagXmlPath(element.getTagPath());
        element.setTagXmlParentPath(element.getTagParentPath());

        if (elementMap.isExist(XML)) {
            MapWrapper map = new MapWrapper(elementMap.toMap(XML), fieldName.concat(PATH_SEPARATOR).concat(XML));
            element.setAttribute(map.toBooleanDefault(IS_ATTRIBUTE, false));
            if (map.isExist(TAG_PATH)) {
                element.setTagXmlPath(map.toString(TAG_PATH, true));
            }
            if (map.isExist(TAG_PARENT_PATH)) {
                element.setTagXmlParentPath(map.toString(TAG_PARENT_PATH, true));
            }
        }

        element.setNormalizeExpression(elementMap.toString(NORMALIZE));
        element.setNormalizeExpressionExist(elementMap.isExist(NORMALIZE));
        element.setForceDefault(elementMap.toBoolean(FORCE_DEFAULT));


        getParsingElements().add(element);
        TypeAdaptor tagTypeAdapter = element.getTagTypeAdapter();
        if (tagTypeAdapter.isObject()) {
            buildObjectField(fieldName, elementMap, element);
        }
        element.validate();

    }

    private void buildObjectField(String fieldName, MapWrapper elementMap, ParsingElement element) {
        elementMap.setParentFieldName(fieldName);
        Map<String, Object> fields;
        if (elementMap.isExist(FIELDS)) {
            fields = elementMap.toMap(FIELDS);
        } else {
            fields = new LinkedHashMap<>();
        }

        List<String> fieldNames = new ArrayList<>();
        int count = 0;
        for (Entry<String, Object> property : fields.entrySet()) {

            count++;
            String propertyName = property.getKey();

            count = setAllFields(element, count, property.getValue(), propertyName);
            fieldNames.add(propertyName);
        }
        element.getTagTypeAdapter().postProcess(element);

    }

    private int setAllFields(ParsingElement element, int count, Object parsingConfig, String propertyName) {
        LinkedHashMap<String, Object> fieldMap = new LinkedHashMap<>();


        if (parsingConfig instanceof Map<?, ?>) {
            buildParsingElementTree(parsingConfig, propertyName, element, count);
        } else if (parsingConfig instanceof List<?>) {
            @SuppressWarnings("unchecked")
            List<Object> parsingConfigList = (List<Object>) parsingConfig;
            for (Object listConfig : parsingConfigList) {
                setAllFields(element, count++, listConfig, propertyName);
            }
        } else if (parsingConfig instanceof String) {
            fieldMap.put(DATA_TYPE, parsingConfig);
            buildParsingElementTree(fieldMap, propertyName, element, count);
        } else {
            buildParsingElementTree(fieldMap, propertyName, element, count);
        }
        return count;
    }

    private void extendsToDefinitions(Map<String, Object> valueMap) {
        if (valueMap.containsKey(REF)) {
            String ref = valueMap.get(REF).toString();
            @SuppressWarnings("unchecked")
            Map<String, Object> definition = (Map<String, Object>) getExpressionResolver().resolveExpression(ref);

            MapUtils.mergeMap(valueMap, definition);
        }
    }

    private void setType(MapWrapper elementMap, ParsingElement element) {
        Object tagType = elementMap.getField(DATA_TYPE);
        Map<String, Object> typeParams = new LinkedHashMap<>(getParams());
        if (tagType != null) {
            if (tagType instanceof Map<?, ?>) {
                MapWrapper typeMap = new MapWrapper(tagType, element.getFieldName().concat(PATH_SEPARATOR).concat(DATA_TYPE));
                element.setType(typeMap.toString("type", true));
                if (typeMap.isExist(PARAMS)) {
                    Map<String, Object> paramsMap = typeMap.toMap(PARAMS);
                    typeParams.putAll(paramsMap);
                }
            } else {
                element.setType(elementMap.toString(DATA_TYPE));
                if (elementMap.isExist(TYPE_PARAMS)) {
                    Map<String, Object> paramsMap = elementMap.toMap(TYPE_PARAMS);
                    typeParams.putAll(paramsMap);

                }
            }
        }
        element.setTypeParameters(typeParams);
    }

    private void setTagType(MapWrapper elementMap, ParsingElement element) {
        Object tagType = elementMap.getField(TAG_TYPE);
        Map<String, Object> tagTypeParams = new LinkedHashMap<>(getParams());
        if (tagType != null) {
            if (tagType instanceof Map<?, ?>) {
                MapWrapper tagTypeMap = new MapWrapper(tagType, element.getFieldName().concat(PATH_SEPARATOR).concat(TAG_TYPE));
                element.setTagType(tagTypeMap.toString("type", true));
                if (tagTypeMap.isExist(PARAMS)) {
                    Map<String, Object> paramsMap = tagTypeMap.toMap(PARAMS);
                    tagTypeParams.putAll(paramsMap);
                }
            } else {
                element.setTagType(elementMap.toString(TAG_TYPE));
                if (elementMap.isExist(TAG_TYPE_PARAMS)) {
                    Map<String, Object> paramsMap = elementMap.toMap(TAG_TYPE_PARAMS);
                    tagTypeParams.putAll(paramsMap);

                }
            }
        }
        element.getTagTypeAdapter().setParameters(tagTypeParams);
    }

    private void fillTransformations(Map<String, Object> transformations) {
        for (Entry<String, Object> transformationRecord : transformations.entrySet()) {

            MapWrapper transformationElement = new MapWrapper(transformationRecord.getValue(), transformationRecord.getKey());
            TransformationElement element = new TransformationElement();
            element.setTransformationCode(transformationRecord.getKey());
            element.setTransformationMap(transformationElement.toMap("map"));
            element.setOnlyIfExist(transformationElement.toBooleanDefault("onlyIfExist", false));
            valueTransformer.add(element);

        }
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public List<ParsingElement> getParsingElements() {
        return parsingElements;
    }

    public void setParsingElements(List<ParsingElement> parsingElements) {
        this.parsingElements = parsingElements;
    }

    public ExpressionResolver getExpressionResolver() {
        return expressionResolver;
    }

    public void setExpressionResolver(ExpressionResolver expressionResolver) {
        this.expressionResolver = expressionResolver;
    }

    @Override
    public boolean isLoaded() {
        return isLoaded;
    }

    public Map<String, Object> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(Map<String, Object> definitions) {
        this.definitions = definitions;
    }

    @Override
    public FunctionContext getFunctionContext() {
        return functionContext;
    }

    @Override
    public ValueTransformer getValueTransformer() {
        return valueTransformer;
    }

}
