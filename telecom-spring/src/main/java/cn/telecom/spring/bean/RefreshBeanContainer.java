package cn.telecom.spring.bean;

import cn.telecom.commons.bean.Refresh;
import cn.telecom.commons.bean.Refreshable;
import cn.telecom.commons.bean.Refreshes;
import com.google.common.collect.ImmutableMap;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RefreshBeanContainer extends AbstractBeanContainer<Refreshable> {

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        Map<String, Refreshable> refreshableMap = new HashMap<>(super.beanMap);
        this.addRefreshAnnoBeans(refreshableMap, super.applicationContext.getBeansWithAnnotation(Refresh.class));
        this.addRefreshesAnnoBeans(refreshableMap, super.applicationContext.getBeansWithAnnotation(Refreshes.class));
        super.beanMap = ImmutableMap.copyOf(refreshableMap);
    }

    protected Refreshable build(Object obj, String method, Refresh refresh) {
        Method m;
        try {
            m = obj.getClass().getDeclaredMethod(method);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return new Refreshable() {
            @Override
            public void refresh() {
                try {
                    m.invoke(obj);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public long timeIntervalMs() {
                return refresh.timeIntervalMs();
            }

            @Override
            public long delay() {
                return refresh.delay();
            }
        };
    }

    protected void addRefreshesAnnoBeans(Map<String, Refreshable> refreshableMap, Map<String, Object> refreshesAnnoBeans) {
        for (Map.Entry<String, Object> entry : refreshesAnnoBeans.entrySet()) {
            Object value = entry.getValue();
            Refreshes annotation = AnnotationUtils.findAnnotation(value.getClass(), Refreshes.class);
            if (annotation == null) {
                continue;
            }
            for (Refresh refresh : annotation.value()) {
                String method = refresh.method();
                refreshableMap.put(entry.getKey() + "#" + method, build(value, method, refresh));
            }
        }

    }

    protected void addRefreshAnnoBeans(Map<String, Refreshable> refreshableMap, Map<String, Object> refreshAnnoBeans) {
        for (Map.Entry<String, Object> entry : refreshAnnoBeans.entrySet()) {
            Object value = entry.getValue();
            Refresh annotation = AnnotationUtils.findAnnotation(value.getClass(), Refresh.class);
            if (annotation == null) {
                continue;
            }
            String method = annotation.method();
            refreshableMap.put(entry.getKey() + "#" + method, build(value, method, annotation));
        }
    }
}
