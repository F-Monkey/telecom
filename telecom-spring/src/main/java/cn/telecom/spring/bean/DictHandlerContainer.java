package cn.telecom.spring.bean;

import com.google.common.collect.ImmutableMap;

import java.util.Collection;
import java.util.stream.Collectors;

public class DictHandlerContainer extends AbstractBeanContainer<DictHandler> {
    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        Collection<DictHandler> beans = super.getBeans();
        super.beanMap = ImmutableMap.copyOf(beans.stream().collect(Collectors.toMap(DictHandler::getCode, f -> f)));
    }
}
