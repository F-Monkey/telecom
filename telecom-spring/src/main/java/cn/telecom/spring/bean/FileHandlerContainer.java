package cn.telecom.spring.bean;

import com.google.common.collect.ImmutableMap;

import java.util.Collection;
import java.util.stream.Collectors;

public class FileHandlerContainer extends AbstractBeanContainer<FileHandler> {
    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        Collection<FileHandler> beans = super.getBeans();
        super.beanMap = ImmutableMap.copyOf(beans.stream().collect(Collectors.toMap(FileHandler::getCode, f -> f)));
    }
}
