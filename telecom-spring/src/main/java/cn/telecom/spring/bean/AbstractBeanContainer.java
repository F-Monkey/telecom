package cn.telecom.spring.bean;

import cn.telecom.commons.util.ClassUtil;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Map;

public abstract class AbstractBeanContainer<T> implements BeanContainer<T>, ApplicationContextAware, InitializingBean {

    protected ApplicationContext applicationContext;

    protected Map<String, T> beanMap;

    protected final Class<T> type;

    public AbstractBeanContainer() {
        this.type = ClassUtil.getActualType(this, AbstractBeanContainer.class, "T");
        this.beanMap = ImmutableMap.of();
    }

    @Override
    public T getBean(String name) {
        return this.beanMap.get(name);
    }

    @Override
    public Collection<T> getBeans() {
        return this.beanMap.values();
    }

    @Override
    public boolean containsBean(String name) {
        return this.beanMap.containsKey(name);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, T> beans = this.applicationContext.getBeansOfType(this.type);
        if (CollectionUtils.isEmpty(beans)) {
            return;
        }
        this.beanMap = ImmutableMap.copyOf(beans);
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

