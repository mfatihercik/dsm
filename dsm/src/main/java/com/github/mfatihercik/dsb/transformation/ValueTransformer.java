package com.github.mfatihercik.dsb.transformation;

@SuppressWarnings("unused")
public interface ValueTransformer {
    String transform(String transformationCode, String sourceValue, boolean transformOnlyIfExist);

    void add(TransformationElement element);

    @SuppressWarnings("unused")
    void remove(String transformationCode);

}
