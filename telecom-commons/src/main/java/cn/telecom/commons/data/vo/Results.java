package cn.telecom.commons.data.vo;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.function.Function;

public interface Results {

    static <T> Result<T> ok(T data) {
        return Result.<T>builder().code(ResultCode.OK).data(data).build();
    }

    static <T> Result<T> ok() {
        return ok(null);
    }

    static <T> Result<T> fail(int code, String msg) {
        return Result.<T>builder().code(code).msg(msg).build();
    }

    static <T> Result<T> fail(String msg) {
        return fail(ResultCode.FAIL, msg);
    }

    static boolean isOK(Result<?> result) {
        return ResultCode.OK == result.getCode();
    }


    static <T> Result<T> error(Throwable error) {
        return Result.<T>builder().code(ResultCode.ERROR).error(error).build();
    }

    static <T> Result<T> unKnown(String msg, T data, Throwable error) {
        return Result.<T>builder().code(ResultCode.UNKNOWN).msg(msg).data(data).error(error).build();
    }

    static <T> Result<T> fromNullData(Result<?> result) {
        return Result.<T>builder().code(result.getCode()).msg(result.getMsg()).error(result.getError()).build();
    }

    static <R, T> Result<R> map(Result<T> result, Function<T, R> func) {
        T data = result.getData();
        if (Objects.isNull(data)) {
            return fromNullData(result);
        }
        R apply = func.apply(data);
        return Result.<R>builder()
                .code(result.getCode())
                .msg(result.getMsg())
                .data(apply)
                .build();
    }

    Type MAP_TYPE = new TypeToken<LinkedHashMap<String, String>>() {
    }.getType();

    static <T> Result<T> fromJsonStr(String s, Gson gson, Class<T> clazz) {
        LinkedHashMap<String, String> json = gson.fromJson(s, MAP_TYPE);
        String code = json.get("code");
        String msg = json.get("msg");
        String data = json.get("data");
        Result.ResultBuilder<T> builder = Result.builder();
        builder.code(Integer.parseInt(code));
        if (!Strings.isNullOrEmpty(msg)) {
            builder.msg(msg);
        }
        if (!Strings.isNullOrEmpty(data)) {
            builder.data(gson.fromJson(data, clazz));
        }
        return builder.build();
    }

    static <T> Result<T> fromJsonStr(String s, Gson gson, Type type) {
        LinkedHashMap<String, String> json = gson.fromJson(s, MAP_TYPE);
        String code = json.get("code");
        String msg = json.get("msg");
        String data = json.get("data");
        Result.ResultBuilder<T> builder = Result.builder();
        builder.code(Integer.parseInt(code));
        if (!Strings.isNullOrEmpty(msg)) {
            builder.msg(msg);
        }
        if (!Strings.isNullOrEmpty(data)) {
            builder.data(gson.fromJson(data, type));
        }
        return builder.build();
    }

    Gson gson = new Gson();

    static <T> Result<T> fromJsonStr(String s, Type type) {
        return fromJsonStr(s, gson, type);
    }

    static String toJson(Result<?> result) {
        return gson.toJson(result);
    }
}
