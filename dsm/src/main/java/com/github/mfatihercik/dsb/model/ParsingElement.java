package com.github.mfatihercik.dsb.model;

import com.github.mfatihercik.dsb.DCMValidationException;
import com.github.mfatihercik.dsb.typeadapter.StdTypeAdapter;
import com.github.mfatihercik.dsb.typeadapter.TypeAdaptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ParsingElement implements Comparable<ParsingElement>, Cloneable {

    public static final String DEFAULT_TAG_TYPE = "STD";
    private ParsingElement parentElement;
    private String parsingTypology;


    private int order;
    private String fieldName;
    private String uniqueKey;
    private String parentPath;
    private String path;
    private String xmlParentPath;
    private String xmlPath;
    private String absolutePath;
    private String dataType;
    private Map<String, Object> dataTypeParameters;
    private String type = DEFAULT_TAG_TYPE;
    private Function function;
    private boolean isTransformEnabled;
    private String transformationCode;
    private boolean isAttribute;
    private boolean isFilterExist;
    private String filter;
    private boolean isNormalizeExist;
    private String normalize;
    private TypeAdaptor tagTypeAdapter;
    private List<ParsingElement> children = new ArrayList<>();
    private int index = -1;
    private Default defaultValue;

    public void addChild(ParsingElement child) {
        child.setParentElement(this);
        this.children.add(child);
    }

    public void validate() {

        String parentFiledName = this.getParentElement() == null ? "" : this.getParentElement().getFieldName();

        String fName = getFieldName();
        throwExceptionIfTrue(fName == null, String.format("fieldName is required for %s", parentFiledName));
        throwExceptionIfTrue(getType() == null, String.format("type is required %s/%s", fName, parentFiledName));
        throwExceptionIfTrue(getPath() == null, String.format("path is required for %s/%s", fName, parentFiledName));
        throwExceptionIfTrue(getUniqueKey() == null, String.format("uniqueKey is required for %s/%s", fName, parentFiledName));

        boolean isFilterExist = (isFilterExist() && getFilter() == null);
        throwExceptionIfTrue(isFilterExist, String.format("filter is required if filterExist true for %s/%s", fName, parentFiledName));

        throwExceptionIfTrue((isTransformEnabled() && getTransformationCode() == null), String.format("transformationCode is required if transformationEnabled true for %s/%s", fName, parentFiledName));

        TypeAdaptor typeAdapter = getTypeAdapter();
        throwExceptionIfTrue(typeAdapter.isObject() && !typeAdapter.isArray() && children.isEmpty(),
                String.format("type of %s/%s is complex type(%s). Object type must have fields ", this.getFieldName(), parentFiledName, this.getType()));
        throwExceptionIfTrue(!typeAdapter.isObject() && !children.isEmpty(),
                String.format("type of %s/%s is not objectType(%s). Only objectType can have fields ", this.getFieldName(), parentFiledName, this.getType()));


        throwExceptionIfTrue(isRoot() && getPath() == null, String.format("path is required for root element for %s/%s", fName, parentFiledName));

    }

    private void throwExceptionIfTrue(boolean condition, String message) {
        if (condition)
            throw new DCMValidationException(message);
    }

    public ParsingElement getParentElement() {
        return parentElement;
    }

    public void setParentElement(ParsingElement parentElement) {
        this.parentElement = parentElement;
        parentElement.children.add(this);
    }

    public String getParsingTypology() {
        return parsingTypology;
    }

    public void setParsingTypology(String parsingTypology) {
        this.parsingTypology = parsingTypology;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public boolean isDefault() {
        return defaultValue != null;
    }


    public Default getDefault() {
        return defaultValue;
    }

    public void setDefault(Default defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getParentPath() {
        return this.parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public String getPath() {
        if (path == null && !isRoot())
            path = getFieldName();
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        if (type == null)
            type = DEFAULT_TAG_TYPE;
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public boolean isTransformEnabled() {
        return isTransformEnabled;
    }

    public void setTransformEnabled(boolean isTransformEnabled) {
        this.isTransformEnabled = isTransformEnabled;
    }

    public String getTransformationCode() {
        return transformationCode;
    }

    public void setTransformationCode(String transformationCode) {
        this.transformationCode = transformationCode;
    }


    public boolean isAttribute() {
        return isAttribute;
    }

    public void setAttribute(boolean isAttribute) {
        this.isAttribute = isAttribute;
    }

    public boolean isFilterExist() {
        return isFilterExist;
    }

    public void setFilterExist(boolean isFilterExist) {
        this.isFilterExist = isFilterExist;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getUniqueKey() {
        if (uniqueKey == null)
            uniqueKey = this.getFieldName();
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public boolean isNormalizeExist() {
        return isNormalizeExist;
    }

    public void setNormalizeExpressionExist(boolean isBeforeExpressionExist) {
        this.isNormalizeExist = isBeforeExpressionExist;
    }

    public String getNormalize() {
        return normalize;
    }

    public void setNormalizeExpression(String beforeExpression) {
        this.normalize = beforeExpression;
    }

    public TypeAdaptor getTypeAdapter() {
        if (tagTypeAdapter == null)
            tagTypeAdapter = new StdTypeAdapter();
        return tagTypeAdapter;
    }

    public void setTypeAdapter(TypeAdaptor tagTypeAdapter) {
        this.tagTypeAdapter = tagTypeAdapter;
    }

    public List<ParsingElement> getChildren() {
        return children;
    }

    public void setChildren(List<ParsingElement> children) {
        this.children = children;
    }

    public boolean isRoot() {
        return parentElement == null;
    }

    public boolean isUseFunction() {
        return function != null;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int compareTo(ParsingElement o) {
        if (this.isDefault() && !o.isDefault())
            return 1;
        if (!this.isDefault() && o.isDefault())
            return -1;
        return (this.order - o.order) % 2;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Map<String, Object> getDataTypeParameters() {
        return dataTypeParameters;
    }

    public void setDataTypeParameters(Map<String, Object> dataTypeParameters) {
        this.dataTypeParameters = dataTypeParameters;
    }

    public String getAbsolutePath() {

        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public String getXmlPath() {
        return xmlPath;
    }

    public void setXmlPath(String xmlPath) {
        this.xmlPath = xmlPath;
    }

    public String getXmlParentPath() {
        return xmlParentPath;
    }

    public void setXmlParentPath(String xmlParentPath) {
        this.xmlParentPath = xmlParentPath;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParsingElement that = (ParsingElement) o;
        return
                Objects.equals(getFieldName(), that.getFieldName()) &&
                        Objects.equals(getUniqueKey(), that.getUniqueKey()) &&
                        Objects.equals(getParentPath(), that.getParentPath()) &&
                        Objects.equals(getPath(), that.getPath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFieldName(), getUniqueKey(), getParentPath(), getPath());
    }
}