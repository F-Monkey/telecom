package cn.telecom.orm.mongo;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;

public class SimpleMongoCrudRepository<T, ID> extends SimpleMongoRepository<T, ID> implements MongoCrudRepository<T, ID>,
        ApplicationContextAware, InitializingBean {

    protected final MongoEntityInformation<T, ID> entityInformation;

    protected final MongoOperations mongoOperations;

    protected ApplicationContext applicationContext;

    /**
     * Creates a new {@link SimpleMongoRepository} for the given {@link MongoEntityInformation} and {@link MongoOperations}.
     *
     * @param metadata        must not be {@literal null}.
     * @param mongoOperations must not be {@literal null}.
     */
    public SimpleMongoCrudRepository(MongoEntityInformation<T, ID> metadata, MongoOperations mongoOperations) {
        super(metadata, mongoOperations);
        this.mongoOperations = mongoOperations;
        this.entityInformation = metadata;
    }

    @Override
    public MongoEntityInformation<T, ID> getEntityInformation() {
        return this.entityInformation;
    }

    @Override
    public MongoOperations getMongoOperations() {
        return this.mongoOperations;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
