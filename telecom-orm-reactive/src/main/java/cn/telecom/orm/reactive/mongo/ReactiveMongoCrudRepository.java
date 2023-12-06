package cn.telecom.orm.reactive.mongo;

import cn.telecom.orm.reactive.IReactiveRepository;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;

@NoRepositoryBean
public interface ReactiveMongoCrudRepository<T, ID> extends IReactiveRepository<T, ID>, ReactiveQueryByExampleExecutor<T> {
    MongoEntityInformation<T, ID> getEntityInformation();

    default String getCollectionName() {
        return this.getEntityInformation().getCollectionName();
    }

    ReactiveMongoOperations getMongoOperations();
}
