package cn.telecom.orm.elasticsearch;

import jakarta.persistence.EntityManager;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchRepositoryFactory;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.io.Serializable;

public class DefaultElasticsearchRepositoryRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable>
        extends ElasticsearchRepositoryFactoryBean<T, S, ID>
        implements ApplicationContextAware {

    protected ApplicationContext applicationContext;

    @Nullable
    protected ElasticsearchOperations operations;

    /**
     * Creates a new {@link JpaRepositoryFactoryBean} for the given repository interface.
     *
     * @param repositoryInterface must not be {@literal null}.
     */
    public DefaultElasticsearchRepositoryRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
        super(repositoryInterface);
    }


    @Override
    public void setElasticsearchOperations(ElasticsearchOperations operations) {

        Assert.notNull(operations, "ElasticsearchOperations must not be null!");
        setMappingContext(operations.getElasticsearchConverter().getMappingContext());
        super.setElasticsearchOperations(operations);
        this.operations = operations;
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        Assert.notNull(operations, "ElasticsearchOperations must be configured!");
    }

    @Override
    protected RepositoryFactorySupport createRepositoryFactory() {
        Assert.notNull(operations, "operations are not initialized");
        DefaultElasticsearchRepositoryFactory defaultElasticsearchRepositoryFactory = new DefaultElasticsearchRepositoryFactory(this.operations);
        defaultElasticsearchRepositoryFactory.setApplicationContext(this.applicationContext);
        return defaultElasticsearchRepositoryFactory;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
