package com.github.mfatihercik.dsb.typeadapter;

import com.github.mfatihercik.dsb.Node;
import com.github.mfatihercik.dsb.ParsingContext;
import com.github.mfatihercik.dsb.PathInfo;
import com.github.mfatihercik.dsb.model.ParsingElement;

import java.util.LinkedHashMap;

public class BaseObjectAdapter extends BaseSimpleAdapter {

    @Override
    public String getValue(ParsingContext parsingContext, Node node, ParsingElement parsingElement, PathInfo pathInfo, String value) {
        return value;
    }


    @Override
    public void setValue(ParsingContext parsingContext, Node currentNode, ParsingElement parsingElement, PathInfo pathInfo, Object value) {
        /*
          this method implemented in sub class
         */
    }


    @Override
    public Object getInitialObject() {
        return new LinkedHashMap<String, Object>();
    }

    @Override
    public boolean isObject() {
        return true;
    }


}
