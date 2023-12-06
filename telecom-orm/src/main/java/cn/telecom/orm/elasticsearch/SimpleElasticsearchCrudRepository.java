package cn.telecom.orm.elasticsearch;

import jakarta.persistence.EntityManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.jpa.repository.support.CrudMethodMetadata;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.lang.Nullable;

public class SimpleElasticsearchCrudRepository<T, ID> extends SimpleJpaRepository<T, ID> implements ElasticsearchCrudRepository<T, ID>,
        ApplicationContextAware, InitializingBean {
    protected final JpaEntityInformation<T, ?> entityInformation;

    protected final EntityManager entityManager;

    protected ApplicationContext applicationContext;

    public SimpleElasticsearchCrudRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityInformation = entityInformation;
        this.entityManager = entityManager;
    }

    public SimpleElasticsearchCrudRepository(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.entityInformation = JpaEntityInformationSupport.getEntityInformation(domainClass, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public JpaEntityInformation<T, ?> getEntityInformation() {
        return this.entityInformation;
    }

    @Override
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    @Nullable
    @Override
    public CrudMethodMetadata getRepositoryMethodMetadata() {
        return super.getRepositoryMethodMetadata();
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
