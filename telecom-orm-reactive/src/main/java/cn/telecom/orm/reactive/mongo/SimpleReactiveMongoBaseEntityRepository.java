package cn.telecom.orm.reactive.mongo;

import cn.telecom.commons.data.BaseEntity;
import cn.telecom.orm.reactive.ReactiveBaseEntityCrudRepository;
import cn.telecom.orm.reactive.ReactiveBeforeCreateBehavior;
import cn.telecom.orm.reactive.ReactiveBeforeUpdateBehavior;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import reactor.core.publisher.Mono;

public class SimpleReactiveMongoBaseEntityRepository<T extends BaseEntity> extends SimpleReactiveMongoCrudRepository<T, String>
        implements ReactiveBaseEntityCrudRepository<T> {

    private ReactiveBeforeUpdateBehavior<T> beforeUpdateBehavior;

    private ReactiveBeforeCreateBehavior<T> beforeCreateBehavior;

    public SimpleReactiveMongoBaseEntityRepository(MongoEntityInformation<T, String> entityInformation, ReactiveMongoOperations mongoOperations) {
        super(entityInformation, mongoOperations);
    }

    @Override
    public <S extends T> Mono<S> save(S entity) {
        MongoEntityInformation<T, String> entityInformation = this.getEntityInformation();
        if (entityInformation.isNew(entity)) {
            return this.insert(entity);
        }
        return this.beforeUpdateBehavior == null ?
                super.save(entity) : this.beforeUpdateBehavior.beforeUpdate(entity).then(super.save(entity));
    }

    @Override
    public <S extends T> Mono<S> insert(S entity) {
        return this.beforeCreateBehavior == null ?
                super.insert(entity) : this.beforeCreateBehavior.beforeCreate(entity).then(super.save(entity));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        if (this.applicationContext == null) {
            return;
        }
        try {
            this.beforeUpdateBehavior = this.applicationContext.getBean(ReactiveBeforeUpdateBehavior.class);
            this.beforeCreateBehavior = this.applicationContext.getBean(ReactiveBeforeCreateBehavior.class);
        } catch (Exception ignore) {
        }
    }
}
