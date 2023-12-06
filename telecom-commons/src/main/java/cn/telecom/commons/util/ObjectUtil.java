package cn.telecom.commons.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

public interface ObjectUtil {

    ConcurrentHashMap<Class<?>, Field[]> FIELD_MAP = new ConcurrentHashMap<>();

    static <T> T mergeNoNullVal(T origin, T _new) {
        Field[] fields = FIELD_MAP.computeIfAbsent(origin.getClass(), (c) -> {
            ArrayList<Field> fieldList = new ArrayList<>(Arrays.asList(c.getDeclaredFields()));
            while ((c = c.getSuperclass()) != Object.class) {
                fieldList.addAll(Arrays.asList(c.getDeclaredFields()));
            }
            for (Field f : fieldList) {
                f.setAccessible(true);
            }
            return fieldList.toArray(new Field[]{});
        });
        for (Field f : fields) {
            try {
                Object o = f.get(_new);
                if (o == null) {
                    f.set(_new, f.get(origin));
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return _new;
    }
}

