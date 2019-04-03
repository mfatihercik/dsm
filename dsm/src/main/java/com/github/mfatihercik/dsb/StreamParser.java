package com.github.mfatihercik.dsb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mfatihercik.dsb.configloader.ConfigConstants;
import com.github.mfatihercik.dsb.expression.ExpressionResolver;
import com.github.mfatihercik.dsb.function.FunctionFactory;
import com.github.mfatihercik.dsb.function.Params;
import com.github.mfatihercik.dsb.model.DeferAssignment;
import com.github.mfatihercik.dsb.model.Function;
import com.github.mfatihercik.dsb.model.ParsingElement;
import com.github.mfatihercik.dsb.transformation.ValueTransformer;
import com.github.mfatihercik.dsb.typeadapter.TypeAdaptor;
import com.github.mfatihercik.dsb.typeconverter.TypeConverterFactory;
import com.github.mfatihercik.dsb.utils.ValidationUtils;

import java.io.InputStream;
import java.io.Reader;
import java.util.*;
import java.util.Map.Entry;

import static com.github.mfatihercik.dsb.configloader.ConfigConstants.PATH_SEPARATOR;

public abstract class StreamParser {


    private static final String VALUE = "value";
    private static final String SELF = "self";
    protected final Map<String, List<ParsingElement>> startElementConfigMaps = new HashMap<>();
    protected final Map<String, List<ParsingElement>> endElementConfigMaps = new HashMap<>();
    protected final Map<String, List<ParsingElement>> defaultValuesElementConfigMaps = new HashMap<>();
    protected final Map<String, List<ParsingElement>> objectParsingElementMaps = new HashMap<>();
    protected final Map<String, List<ParsingElement>> cacheStartDefaultEventConfigMaps = new HashMap<>();
    protected final Map<String, List<ParsingElement>> cacheEndDefaultEventConfigMaps = new HashMap<>();
    protected final PathInfo pathInfo = new PathInfo();
    private final ParsingContext parsingContext = new ParsingContext();
    private final String[] parentPathStack = new String[32];
    protected ValueTransformer valueTransformer = null;
    protected ExpressionResolver expressionResolver;
    protected FunctionFactory functionFactory;
    protected TypeConverterFactory typeConverterFactory;
    protected Node nodes = new Node();
    protected String currentTagName;
    protected String tempValue;
    private PathGenerator absolutePathGenerator;
    private List<ParsingElement> allParsingElement = null;
    private Map<String, Object> params = new LinkedHashMap<>();
    private ObjectMapper objectMapper;
    private Class<?> resultType = null;
    private List<String> uniqueKeyIndex = new ArrayList<>();
    private String parentPath = PATH_SEPARATOR;
    private int parentPathStackSize = 0;

    {
        parentPathStack[parentPathStackSize] = parentPath;
    }

    public StreamParser(FunctionFactory functionFactory, ExpressionResolver expressionResolver, PathGenerator absolutePathGenerator, ObjectMapper objectMapper, Class<?> resultType, TypeConverterFactory typeConverterFactory) {
        this.absolutePathGenerator = absolutePathGenerator;
        this.expressionResolver = expressionResolver;
        this.functionFactory = functionFactory;
        this.objectMapper = objectMapper;
        this.typeConverterFactory = typeConverterFactory;
        this.setResultType(resultType);

    }

    protected void clear() {
        startElementConfigMaps.clear();
        endElementConfigMaps.clear();
        defaultValuesElementConfigMaps.clear();
        objectParsingElementMaps.clear();
        parsingContext.clear();
        nodes.setChild(null);

    }

    public void init() {
        clear();
        splitParsingElementsIntoMaps();
        initExpressionResolver(getParams());
        initParsingElementContext();
    }

    protected void initParsingElementContext() {
        parsingContext.initNodeMap(this.getUniqueKeyIndex().size());

    }

    protected void calculateIndexOfParsingElement(ParsingElement parsingElement) {
        String uniqueKey = parsingElement.getUniqueKey();
        int index = getUniqueKeyIndex().indexOf(uniqueKey);
        if (index > -1) {
            parsingElement.setIndex(index);
        } else {
            parsingElement.setIndex(getUniqueKeyIndex().size());
            getUniqueKeyIndex().add(uniqueKey);
        }
    }

    private void splitParsingElementsIntoMaps() {

        for (ParsingElement parsingElement : getAllParsingElement()) {
            parsingElement = initParsingElement(parsingElement);
            TypeAdaptor typeAdapter = parsingElement.getTypeAdapter();
            String key = generateKey(parsingElement);
            if (typeAdapter.isObject()) {
                calculateIndexOfParsingElement(parsingElement);
                putParsingElementToToMap(this.objectParsingElementMaps, key, parsingElement);
                continue;
            }

            if (parsingElement.isDefault()) {
                putParsingElementToToMap(this.defaultValuesElementConfigMaps, parsingElement.getAbsolutePath(), parsingElement);
            }
            if (isStartElement(parsingElement)) {
                putParsingElementToToMap(this.startElementConfigMaps, key, parsingElement);
            } else {
                putParsingElementToToMap(this.endElementConfigMaps, key, parsingElement);
            }

        }
    }

    protected ParsingElement initParsingElement(ParsingElement parsingElement) {

        parsingElement.setAbsolutePath(absolutePathGenerator.generatePath(parsingElement));

        return parsingElement;


    }

    protected void parentTagAdd(String qName) {
        if (parentPath != PATH_SEPARATOR) {
            parentPath = parentPath.concat(PATH_SEPARATOR);
        }
        parentPath = parentPath.concat(qName);
        parentPathStackSize++;
        parentPathStack[parentPathStackSize] = parentPath;
    }

    protected void parentTagRemove(String qName) {
        parentPathStack[parentPathStackSize] = null;
        parentPathStackSize--;
        parentPath = parentPathStack[parentPathStackSize];
    }

    protected String generateKey(ParsingElement parsingElement) {
        if (parsingElement.isAttribute()) {
            return getAttributePath(parsingElement);
        }
        return absolutePathGenerator.concatPaths(parsingElement.getAbsolutePath(), parsingElement.getPath());
    }

    protected String getAttributePath(ParsingElement parsingElement) {
        return parsingElement.getAbsolutePath();
    }

    protected boolean isStartElement(ParsingElement parsingElement) {
        return parsingElement.getTypeAdapter().isStartElement() || parsingElement.isAttribute();
    }

    protected void initExpressionResolver(Map<String, Object> paramsMap) {
        expressionResolver.setBean("all", parsingContext.getMainNodeMap());
        expressionResolver.setBean("params", paramsMap);
    }

    protected void putParsingElementToToMap(Map<String, List<ParsingElement>> defaultValueMap, String key, ParsingElement parsingElement) {
        if (defaultValueMap.containsKey(key)) {
            List<ParsingElement> list = defaultValueMap.get(key);
            list.add(parsingElement);
        } else {
            ArrayList<ParsingElement> arrayList = new ArrayList<>();
            arrayList.add(parsingElement);
            defaultValueMap.put(key, arrayList);
        }
    }

    public Object parse(InputStream stream) {
        resetForParse();
        try {
            parseInputStream(stream);
        } finally {
            parsingContext.clear();
        }
        return getData();
    }

    private void resetForParse() {
        nodes = new Node();
        nodes.setObjectMapper(getObjectMapper());
        parsingContext.setRootNode(nodes);
    }

    public Object parse(Reader reader) {
        resetForParse();
        try {
            parseInputStream(reader);
        } finally {
            parsingContext.clear();
        }
        return getData();
    }

    protected Node registerNewNode(ParsingElement parsingElement, PathInfo pathInfo) {
        Node node = parsingElement.getTypeAdapter().registerNode(parsingContext, parsingElement, pathInfo);
        Node parentNode = parsingElement.getTypeAdapter().getParentNode(parsingContext, parsingElement);
        if (isDeferredAssignment(parsingElement, parentNode)) {
            parsingContext.addDeferAssignment(parsingElement, pathInfo, null);
        }
        evaluateDeferredAssignment(parsingElement, pathInfo, node);
        return node;

    }

    private void evaluateDeferredAssignment(ParsingElement parsingElement, PathInfo pathInfo, Node node) {
        List<DeferAssignment> deferAssignment = parsingContext.getDeferredAssignment(parsingElement);
        if (deferAssignment != null) {
            for (DeferAssignment assignment : deferAssignment) {
                ParsingElement currentParsingElement = assignment.getCurrentParsingElement();
                if (currentParsingElement.getTypeAdapter().isObject()) {
                    node.set(currentParsingElement.getFieldName(), parsingContext.get(currentParsingElement).getData());
                } else if (assignment.isDefault()) {
                    setDefaultValueOnNode(currentParsingElement, currentParsingElement.getDefault().getValue(), assignment.getPathInfo());
                } else {

                    setValueOnNode(currentParsingElement, assignment.getPathInfo(), (String) assignment.getValue());
                }
            }
        }
    }

    protected void setValueOnNode(ParsingElement parsingElement, PathInfo pathInfo, String value) {

        TypeAdaptor typeAdapter = parsingElement.getTypeAdapter();

        Node node = getCurrentNode(parsingElement);
        if (node == null) {
            //noinspection ConstantConditions
            ValidationUtils.assertTrue(true, String.format("Node for %s/%s parsing element is not registered.", parsingElement.getFieldName(), parsingElement.getPath()));
        }
        if (isDeferredAssignment(parsingElement, node)) {
            parsingContext.addDeferAssignment(parsingElement, pathInfo, value);
            return;
        }
        value = typeAdapter.getValue(parsingContext, node, parsingElement, pathInfo, value);
        value = evaluateBeforeExpression(parsingElement, value, node);
        if (parsingElement.isFilterExist()) {
            Boolean isFilterTrue = evaluateFilterExpression(parsingElement, value, node);
            if (!isFilterTrue) {
                if (typeAdapter.isObject()) {
                    removeFilteredNode(parsingElement, node, pathInfo);
                }
                return;
            }

        }
        if (parsingElement.isTransformEnabled()) {
            value = transformValue(parsingElement, value);
        }
        Object convertedValue = convertValue(parsingElement, value);
        typeAdapter.setValue(parsingContext, node, parsingElement, pathInfo, convertedValue);
        if (parsingElement.isUseFunction()) {
            Function function = parsingElement.getFunction();
            getFunctionFactory().execute(function.getName(), new Params(parsingContext, parsingElement, node, pathInfo, value, function.getParams()));
        }

    }

    private boolean isDeferredAssignment(ParsingElement parsingElement, Node node) {
        return node.isRoot() && !parsingElement.isRoot();
    }

    private Node getCurrentNode(ParsingElement parsingElement) {
        TypeAdaptor typeAdapter = parsingElement.getTypeAdapter();
        return typeAdapter.getCurrentNode(parsingContext, parsingElement);


    }

    protected Object convertValue(ParsingElement parsingElement, String value) {
        String dataType = parsingElement.getDataType();
        if (dataType == null)
            dataType = "default";
        return getTypeConverterFactory().getTypeConverter(dataType).convert(value, parsingElement.getDataTypeParameters());
    }

    protected String transformValue(ParsingElement parsingElement, String value) {
        String sourceValue = value;
        value = valueTransformer.transform(parsingElement.getTransformationCode(), sourceValue, false);
        return value;
    }

    protected void removeFilteredNode(ParsingElement parsingElement, Node currentNode, PathInfo pathInfo) {
        parsingElement.getTypeAdapter().deregisterNode(parsingContext, currentNode, parsingElement, pathInfo);
    }

    protected Boolean evaluateFilterExpression(ParsingElement parsingElement, Object value, Node currentNode) {
        expressionResolver.setBean(SELF, currentNode);
        expressionResolver.setBean(VALUE, value);
        Object resolved = expressionResolver.resolveExpression(parsingElement.getFilter());
        return (resolved == null ? false : Boolean.valueOf(resolved.toString()));
    }

    protected String evaluateBeforeExpression(ParsingElement parsingElement, String value, Node currentNode) {
        if (parsingElement.isNormalizeExist()) {
            expressionResolver.setBean(SELF, currentNode);
            expressionResolver.setBean(VALUE, value);
            Object resolved = expressionResolver.resolveExpression(parsingElement.getNormalize());
            value = resolved == null ? null : resolved.toString();
        }
        return value;
    }

    protected void setDefaultValueOnNode(ParsingElement parsingElement, String value, PathInfo pathInfo) {
        TypeAdaptor typeAdapter = parsingElement.getTypeAdapter();
        Node node = getCurrentNode(parsingElement);

        if (node == null)
            return;

        if (isDeferredAssignment(parsingElement, node)) {
            parsingContext.addDeferAssignment(parsingElement, pathInfo, value, true);
            //TODO write test for default deferred assignment
            return;
        }
        boolean overwrite = parsingElement.getDefault().isForce() || !node.containsKey(parsingElement.getFieldName());
        if (!overwrite)
            return;
        Object resolvedValue = value;
        if (value != null && value.startsWith("$")) {

            expressionResolver.setBean(SELF, node);
            expressionResolver.setBean(VALUE, value);
            resolvedValue = expressionResolver.resolveExpression(value);
        }
        if (parsingElement.isFilterExist()) {
            Boolean isFilterTrue = evaluateFilterExpression(parsingElement, resolvedValue, node);
            if (!isFilterTrue) {
                return;
            }
        }
        if (resolvedValue instanceof String) {
            String resolvedValueString = (String) resolvedValue;
            if (parsingElement.isTransformEnabled()) {
                resolvedValueString = transformValue(parsingElement, resolvedValueString);
            }
            resolvedValue = convertValue(parsingElement, resolvedValueString);
        }
        typeAdapter.setValue(parsingContext, node, parsingElement, pathInfo, resolvedValue);
        if (parsingElement.isUseFunction()) {
            Function function = parsingElement.getFunction();
            getFunctionFactory().execute(function.getName(), new Params(parsingContext, parsingElement, node, pathInfo, resolvedValue, function.getParams()));
        }

    }

    protected List<ParsingElement> getParsingElements(Map<String, List<ParsingElement>> elementConfigMaps, String generateKey) {

        List<ParsingElement> selectedConfigs = new LinkedList<>();

        for (Entry<String, List<ParsingElement>> entry : elementConfigMaps.entrySet()) {
            if (generateKey.matches(entry.getKey())) {
                selectedConfigs.addAll(entry.getValue());
            }
        }
        Collections.sort(selectedConfigs);
        return selectedConfigs;
    }

    protected List<ParsingElement> getStartDefaultEventElements(String genderedKey) {

        List<ParsingElement> parsingElements = cacheStartDefaultEventConfigMaps.get(genderedKey);
        if (parsingElements == null) {


            parsingElements = getParsingElements(defaultValuesElementConfigMaps, genderedKey);
            for (Iterator<ParsingElement> iterator = parsingElements.iterator(); iterator.hasNext(); ) {
                if (!iterator.next().getDefault().isAtStart()) {
                    iterator.remove();
                }
            }
            cacheStartDefaultEventConfigMaps.put(genderedKey, parsingElements);
        }
        return parsingElements;
    }

    protected void evaluateStartDefaultValue(PathInfo pathInfo, String genderedKey) {
        List<ParsingElement> defaultEventElements = getStartDefaultEventElements(genderedKey);
        for (ParsingElement element : defaultEventElements) {
            setDefaultValueOnNode(element, element.getDefault().getValue(), pathInfo);
        }
    }

    protected void evaluateEndDefaultValue(PathInfo pathInfo, String genderedKey) {
        List<ParsingElement> defaultEventElements = getEndDefaultEventElements(genderedKey);
        for (ParsingElement element : defaultEventElements) {
            setDefaultValueOnNode(element, element.getDefault().getValue(), pathInfo);
        }
    }

    protected List<ParsingElement> getEndDefaultEventElements(String genderedKey) {

        List<ParsingElement> parsingElements = cacheEndDefaultEventConfigMaps.get(genderedKey);

        if (parsingElements == null) {
            parsingElements = getParsingElements(defaultValuesElementConfigMaps, genderedKey);
            cacheEndDefaultEventConfigMaps.put(genderedKey, parsingElements);
        }
        return parsingElements;
    }

    protected String generateKey(String tagField, String tagParent) {
        if (tagParent == null)
            return tagField;
        return tagParent.concat(PATH_SEPARATOR).concat(tagField);

    }

    protected String parentTag() {
        return parentPath;
    }

    public Object getData() {
        return getNodes().get(ConfigConstants.RESULT);
    }


    public ValueTransformer getTransformer() {
        return valueTransformer;
    }

    public void setValueTransformer(ValueTransformer transformer) {
        this.valueTransformer = transformer;
    }

    protected Node getNodes() {
        return nodes;
    }

    public ExpressionResolver getExpressionResolver() {
        return expressionResolver;
    }

    public void setExpressionResolver(ExpressionResolver expressionResolver) {
        this.expressionResolver = expressionResolver;
    }

    public FunctionFactory getFunctionFactory() {
        return functionFactory;
    }

    public void setFunctionFactory(FunctionFactory functionFactory) {
        this.functionFactory = functionFactory;
    }

    public PathGenerator getAbsolutePathGenerator() {
        return absolutePathGenerator;
    }

    public void setAbsolutePathGenerator(PathGenerator absolutePathGenerator) {
        this.absolutePathGenerator = absolutePathGenerator;
    }

    protected abstract void parseInputStream(InputStream inputStream);

    protected abstract void parseInputStream(Reader reader);

    public List<ParsingElement> getAllParsingElement() {
        return allParsingElement;
    }

    public void setAllParsingElement(List<ParsingElement> allParsingElement) {
        this.allParsingElement = allParsingElement;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public List<String> getUniqueKeyIndex() {
        return uniqueKeyIndex;
    }

    public void setUniqueKeyIndex(List<String> uniqueKeyIndex) {
        this.uniqueKeyIndex = uniqueKeyIndex;
    }

    public Class<?> getResultType() {
        return resultType;
    }

    public void setResultType(Class<?> resultType) {
        this.resultType = resultType;
        this.parsingContext.setResultType(resultType);
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    public TypeConverterFactory getTypeConverterFactory() {
        return typeConverterFactory;
    }

    public void setTypeConverterFactory(TypeConverterFactory typeConverterFactory) {
        this.typeConverterFactory = typeConverterFactory;
    }
}
