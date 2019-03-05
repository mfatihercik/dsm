package com.github.mfatihercik.dsb.typeadapter;

import com.github.mfatihercik.dsb.Node;
import com.github.mfatihercik.dsb.ParsingContext;
import com.github.mfatihercik.dsb.ParsingElement;
import com.github.mfatihercik.dsb.PathInfo;
import com.github.mfatihercik.dsb.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JoinTypeAdapter extends BaseSimpleAdapter {

    private static final String SEPARATOR = "separator";
    private static final String DEFAULT_SEPARATOR = ",";
    private static final String FIELDS = "fields";
    protected Map<String, Object> params = null;

    @Override
    public Map<String, Object> getParameters() {
        return this.params;
    }

    @Override
    public void setParameters(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public String getValue(ParsingContext parsingContext, Node node, ParsingElement parsingElement, PathInfo pathInfo, String value) {
        Map<String, Object> parameters = getParameters();
        assert parameters != null : "Join tagType must have params";
        assert parameters.containsKey(FIELDS) : "Join tagType must have \"fields\" in params";
        Object object = parameters.get(FIELDS);
        String separator = parameters.containsKey(SEPARATOR) ? parameters.get(SEPARATOR).toString() : DEFAULT_SEPARATOR;
        assert object instanceof List : "\"fields\" must be List";
        @SuppressWarnings("unchecked")
        List<Object> fields = (List<Object>) object;
        List<String> values = new ArrayList<>();
        for (Object field : fields) {
            if (!node.containsKey(field.toString())) {
                continue;
            }
            values.add(String.valueOf(node.get(field.toString())));
        }
        return StringUtils.join(values, separator);

    }
}
