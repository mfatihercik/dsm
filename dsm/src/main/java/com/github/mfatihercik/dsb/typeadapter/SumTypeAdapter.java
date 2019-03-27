package com.github.mfatihercik.dsb.typeadapter;

import com.github.mfatihercik.dsb.Node;
import com.github.mfatihercik.dsb.ParsingContext;
import com.github.mfatihercik.dsb.PathInfo;
import com.github.mfatihercik.dsb.model.ParsingElement;
import com.github.mfatihercik.dsb.utils.ValidationUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class SumTypeAdapter extends BaseSimpleAdapter {


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
    public String getValue(ParsingContext parsingContext, Node node, ParsingElement parsingElement, PathInfo pathInfo, String tagValue) {
        Map<String, Object> parameters = getParameters();

        ValidationUtils.assertTrue(parameters == null, "Sum type must have params");
        ValidationUtils.assertTrue(!parameters.containsKey(FIELDS), "Sum type must have \"fields\" in params");
        Object object = parameters.get(FIELDS);
        ValidationUtils.assertTrue(!(object instanceof List), "\"fields\" must be List");
        @SuppressWarnings("unchecked")
        List<Object> fields = (List<Object>) object;
        BigDecimal total = BigDecimal.ZERO;
        for (Object field : fields) {
            if (!node.containsKey(field.toString())) {
                continue;
            }
            String value = node.get(field.toString()).toString();
            total = total.add(new BigDecimal(value));
        }
        return total.toPlainString();

    }
}
