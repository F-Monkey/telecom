package cn.telecom.orm.mongo;

import cn.telecom.commons.data.BaseEntity;
import cn.telecom.orm.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;

public class SimpleMongoBaseEntityRepository<T extends BaseEntity> extends SimpleMongoCrudRepository<T, String>
        implements BaseEntityCrudRepository<T>, ApplicationContextAware, InitializingBean {

    protected final MongoEntityInformation<T, String> entityInformation;
    protected final MongoOperations mongoOperations;
    private BeforeUpdateBehavior<BaseEntity> beforeUpdateBehavior;

    private BeforeCreateBehavior<BaseEntity> beforeCreateBehavior;

    /**
     * Creates a new {@link SimpleMongoRepository} for the given {@link MongoEntityInformation} and {@link MongoOperations}.
     *
     * @param metadata        must not be {@literal null}.
     * @param mongoOperations must not be {@literal null}.
     */
    public SimpleMongoBaseEntityRepository(MongoEntityInformation<T, String> metadata, MongoOperations mongoOperations) {
        super(metadata, mongoOperations);
        this.entityInformation = metadata;
        this.mongoOperations = mongoOperations;
    }

    @Override
    public <S extends T> S save(S entity) {
        if (this.entityInformation.isNew(entity)) {
            return this.insert(entity);
        }
        if (this.beforeUpdateBehavior != null) {
            this.beforeUpdateBehavior.beforeUpdate(entity);
        }
        return super.save(entity);
    }

    @Override
    public <S extends T> S insert(S entity) {
        if (this.beforeCreateBehavior != null) {
            this.beforeCreateBehavior.beforeCreate(entity);
        }
        return super.insert(entity);
    }

    @Override
    public void afterPropertiesSet() {
        if (this.applicationContext == null) {
            return;
        }
        try {
            this.beforeUpdateBehavior = this.applicationContext.getBean(BaseEntityBeforeUpdateBehavior.class);
            this.beforeCreateBehavior = this.applicationContext.getBean(BaseEntityBeforeCreateBehavior.class);
        } catch (Exception ignore) {
        }
    }
}
