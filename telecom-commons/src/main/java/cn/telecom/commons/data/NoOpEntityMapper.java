package cn.telecom.commons.data;

public interface NoOpEntityMapper<T> extends EntityMapper<T, T, T> {
    @Override
    default T copyFromDto(T t) {
        return t;
    }

    @Override
    default T copyToVo(T t) {
        return t;
    }
}
