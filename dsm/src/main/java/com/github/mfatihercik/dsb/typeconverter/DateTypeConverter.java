package com.github.mfatihercik.dsb.typeconverter;

import com.github.mfatihercik.dsb.utils.ValidationUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TimeZone;

public class DateTypeConverter implements TypeConverter {

    @Override
    public Object convert(String value, Map<String, Object> params) {
        ValidationUtils.assertTrue(params == null, "params is required for Date type");
        Object dateFormat = params.get("dateFormat");
        ValidationUtils.assertTrue(dateFormat == null, "dateFormat is not defined in parameters");
        SimpleDateFormat format = new SimpleDateFormat(dateFormat.toString());
        Object timeZone = params.get("timeZone");
        if (timeZone != null) {
            format.setTimeZone(TimeZone.getTimeZone(timeZone.toString()));
        }

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
