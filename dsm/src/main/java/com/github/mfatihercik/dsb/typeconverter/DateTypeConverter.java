package com.github.mfatihercik.dsb.typeconverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TimeZone;

public class DateTypeConverter implements TypeConverter {

    @Override
    public Object convert(String value, Map<String, Object> params) {
        assert params != null : "params is required for Date type";
        assert params.get("dateFormat") != null : "dateFormat is not defined in parameters";
        SimpleDateFormat format = new SimpleDateFormat(params.get("dateFormat").toString());
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        if (value != null) {
            try {
                return format.parse(value);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        return null;

    }

}
