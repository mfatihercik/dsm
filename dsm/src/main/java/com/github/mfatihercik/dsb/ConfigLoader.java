package com.github.mfatihercik.dsb;

import com.github.mfatihercik.dsb.function.FunctionContext;
import com.github.mfatihercik.dsb.model.ParsingElement;
import com.github.mfatihercik.dsb.transformation.ValueTransformer;

import java.util.List;
import java.util.Map;

public interface ConfigLoader {

    Map<String, Object> getParams();

    FunctionContext getFunctionContext();

    ValueTransformer getValueTransformer();

    List<ParsingElement> load(boolean reload);

    boolean isLoaded();
}
