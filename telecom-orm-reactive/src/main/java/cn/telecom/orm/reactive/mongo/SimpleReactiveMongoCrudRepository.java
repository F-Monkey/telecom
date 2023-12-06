package cn.telecom.orm.reactive.mongo;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleReactiveMongoRepository;
import org.springframework.lang.NonNull;
import reactor.core.publisher.Mono;

import java.io.Serializable;

public class SimpleReactiveMongoCrudRepository<T, ID extends Serializable> extends SimpleReactiveMongoRepository<T, ID> implements ReactiveMongoCrudRepository<T, ID>,
        ApplicationContextAware, InitializingBean {

    protected ApplicationContext applicationContext;

    protected final MongoEntityInformation<T, ID> entityInformation;

    protected final ReactiveMongoOperations reactiveMongoOperations;

    public SimpleReactiveMongoCrudRepository(MongoEntityInformation<T, ID> entityInformation, ReactiveMongoOperations mongoOperations) {
        super(entityInformation, mongoOperations);
        this.entityInformation = entityInformation;
        this.reactiveMongoOperations = mongoOperations;
    }

    @Override
    public Mono<Page<T>> findPage(Pageable pageable) {
        return null;
    }

    @Override
    public MongoEntityInformation<T, ID> getEntityInformation() {
        return this.entityInformation;
    }

    @Override
    public ReactiveMongoOperations getMongoOperations() {
        return this.reactiveMongoOperations;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}