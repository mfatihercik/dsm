package com.github.mfatihercik.dsb.model;

import com.github.mfatihercik.dsb.DCMValidationException;
import com.github.mfatihercik.dsb.typeadapter.TypeAdaptor;
import com.github.mfatihercik.dsb.typeadapter.TypeAdaptorFactory;

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
    private String tagParentPath;
    private String tagPath;
    private String tagXmlParentPath;
    private String tagXmlPath;
    private String tagAbsolutePath;
    private String type;
    private Map<String, Object> typeParameters;
    private String tagType = DEFAULT_TAG_TYPE;
    private String tagParameters;
    private Function function;
    private boolean isTransformEnabled;
    private String transformationCode;
    private boolean isAttribute;
    private boolean isOverwrite;
    private boolean isFilterExist;
    private String filterExpression;
    private boolean isBeforeExpressionExist;
    private String beforeExpression;
    private String comment;
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
        throwExceptionIfTrue(getTagType() == null, String.format("tagType is required %s/%s", fName, parentFiledName));
        throwExceptionIfTrue(getTagPath() == null, String.format("tagPath is required for %s/%s", fName, parentFiledName));
        throwExceptionIfTrue(getUniqueKey() == null, String.format("uniqueKey is required for %s/%s", fName, parentFiledName));

        boolean isFilterExist = (isFilterExist() && getFilterExpression() == null);
        throwExceptionIfTrue(isFilterExist, String.format("filterExpression is required if filterExist true for %s/%s", fName, parentFiledName));

        throwExceptionIfTrue((isTransformEnabled() && getTransformationCode() == null), String.format("transformationCode is required if transformationEnabled true for %s/%s", fName, parentFiledName));

        TypeAdaptor typeAdapter = getTagTypeAdapter();
        throwExceptionIfTrue(typeAdapter.isObject() && !typeAdapter.isArray() && children.isEmpty(),
                String.format("tagType of %s/%s is complex tagType(%s). Object tagType must have fields ", this.getFieldName(), parentFiledName, this.getTagType()));
        throwExceptionIfTrue(!typeAdapter.isObject() && !children.isEmpty(),
                String.format("tagType of %s/%s is not objectType(%s). Only objectType can have fields ", this.getFieldName(), parentFiledName, this.getTagType()));


        throwExceptionIfTrue(isRoot() && getTagPath() == null, String.format("tagPath is required for root element for %s/%s", fName, parentFiledName));

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

    public String getTagParentPath() {
        return this.tagParentPath;
    }

    public void setTagParentPath(String tagParentPath) {
        this.tagParentPath = tagParentPath;
    }

    public String getTagPath() {
        if (tagPath == null && !isRoot())
            tagPath = getFieldName();
        return tagPath;
    }

    public void setTagPath(String tagPath) {
        this.tagPath = tagPath;
    }

    public String getTagType() {
        if (tagType == null)
            tagType = DEFAULT_TAG_TYPE;
        return tagType;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }

    public String getTagParameters() {
        return tagParameters;
    }

    public void setTagParameters(String tagParameters) {
        this.tagParameters = tagParameters;
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

    public String getFilterExpression() {
        return filterExpression;
    }

    public void setFilterExpression(String filterExpression) {
        this.filterExpression = filterExpression;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUniqueKey() {
        if (uniqueKey == null)
            uniqueKey = this.getFieldName();
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public boolean isBeforeExpressionExist() {
        return isBeforeExpressionExist;
    }

    public void setNormalizeExpressionExist(boolean isBeforeExpressionExist) {
        this.isBeforeExpressionExist = isBeforeExpressionExist;
    }

    public String getBeforeExpression() {
        return beforeExpression;
    }

    public void setNormalizeExpression(String beforeExpression) {
        this.beforeExpression = beforeExpression;
    }

    public TypeAdaptor getTagTypeAdapter() {
        if (tagTypeAdapter == null) {
            tagTypeAdapter = TypeAdaptorFactory.getTypeAdapter(getTagType());
        }
        return tagTypeAdapter;
    }

    public void setTagTypeAdapter(TypeAdaptor tagTypeAdapter) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getTypeParameters() {
        return typeParameters;
    }

    public void setTypeParameters(Map<String, Object> typeParameters) {
        this.typeParameters = typeParameters;
    }

    public String getTagAbsolutePath() {

        return tagAbsolutePath;
    }

    public void setTagAbsolutePath(String tagAbsolutePath) {
        this.tagAbsolutePath = tagAbsolutePath;
    }

    public String getTagXmlPath() {
        return tagXmlPath;
    }

    public void setTagXmlPath(String tagXmlPath) {
        this.tagXmlPath = tagXmlPath;
    }

    public String getTagXmlParentPath() {
        return tagXmlParentPath;
    }

    public void setTagXmlParentPath(String tagXmlParentPath) {
        this.tagXmlParentPath = tagXmlParentPath;
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
                        Objects.equals(getTagParentPath(), that.getTagParentPath()) &&
                        Objects.equals(getTagPath(), that.getTagPath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFieldName(), getUniqueKey(), getTagParentPath(), getTagPath());
    }
}