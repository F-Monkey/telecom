package cn.telecom.spring.bean;

import java.util.Collection;

public interface BeanContainer<T> {
    T getBean(String name);

    Collection<T> getBeans();

    boolean containsBean(String name);
}
