package com.github.mfatihercik.dsb.utils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MapUtils {
    @SuppressWarnings("unchecked")
    public static void mergeMap(Map<String, Object> mainMap, Map<String, Object> mapToMerge) {
        Set<Entry<String, Object>> entrySet = mapToMerge.entrySet();
        Map<String, Object> temp = new LinkedHashMap<>();
        for (Entry<String, Object> entry : entrySet) {
            String key = entry.getKey();
            Object target = entry.getValue();
            if (mainMap.containsKey(key)) {
                Object source = mainMap.get(key);


                if (source instanceof Map<?, ?>) {
                    if (target instanceof Map<?, ?>)
                        mergeMap((Map<String, Object>) source, (Map<String, Object>) target);
                    else if (target instanceof List<?>) {
                        List<Object> list = (List<Object>) target;
                        list.add(source);
                        mainMap.put(key, list);
                    }
                } else if (source instanceof List<?>) {
                    List<Object> list = (List<Object>) source;
                    if (target instanceof List<?>) {
                        list.addAll(0, (List<Object>) target);
                    } else {
                        list.add(0, target);
                    }
                }

            } else {
                temp.put(key, target);
            }
        }
        temp.putAll(mainMap);
        mainMap.clear();
        mainMap.putAll(temp);

    }
}
