package cn.telecom.orm.mongo;

import cn.telecom.orm.IRepository;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.QueryByExampleExecutor;

@NoRepositoryBean
public interface MongoCrudRepository<T, ID> extends IRepository<T, ID>, QueryByExampleExecutor<T> {
    MongoEntityInformation<T, ID> getEntityInformation();

    default String getCollectionName() {
        return this.getEntityInformation().getCollectionName();
    }

    MongoOperations getMongoOperations();
}
