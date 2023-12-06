package cn.telecom.commons.data;

import cn.telecom.commons.util.ObjectUtil;
import org.mapstruct.Named;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public interface EntityMapper<D, T, V> {
    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    T copyFromDto(D d);

    V copyToVo(T t);

    default T mergeNoNullVal(T origin, T _new) {
        return ObjectUtil.mergeNoNullVal(origin, _new);
    }


    @Named("long2DateString")
    default String toDateString(Long timeStamp) {
        if (timeStamp == null) {
            return "";
        }
        return Instant.ofEpochMilli(timeStamp).atZone(ZoneId.systemDefault()).format(DATE_TIME_FORMATTER);
    }
}
