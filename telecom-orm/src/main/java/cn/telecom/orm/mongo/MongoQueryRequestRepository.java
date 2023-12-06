package cn.telecom.orm.mongo;

import cn.telecom.commons.data.QueryRequest;
import cn.telecom.orm.QueryRequestRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface MongoQueryRequestRepository<Q extends QueryRequest, T, ID> extends QueryRequestRepository<Q, T, ID>, MongoCrudRepository<T, ID> {
    default Query buildQuery(Q q) {
        return new Query();
    }
    @Override
    default Page<T> selectPageByQueryRequest(Q q, Pageable pageable) {
        Query query = this.buildQuery(q);
        query.with(pageable);
        MongoEntityInformation<T, ID> entityInformation = this.getEntityInformation();
        Class<T> type = entityInformation.getJavaType();
        String collectionName = entityInformation.getCollectionName();
        MongoOperations mongoOperations = this.getMongoOperations();
        List<T> list = mongoOperations.find(query, type, collectionName);
        return PageableExecutionUtils.getPage(list, pageable, () -> mongoOperations
                .count(Query.of(query).limit(-1).skip(-1), type, collectionName));
    }

    @Override
    default List<T> selectByQueryRequest(Q q, Sort sort) {
        Query build = this.buildQuery(q);
        MongoOperations mongoOperations = this.getMongoOperations();
        MongoEntityInformation<T, ID> entityInformation = this.getEntityInformation();
        Class<T> javaType = entityInformation.getJavaType();
        String collectionName = entityInformation.getCollectionName();
        return mongoOperations.find(build, javaType, collectionName);
    }

    @Override
    default Optional<T> selectOneByQueryRequest(Q q) {
        Query query = this.buildQuery(q);
        MongoOperations mongoOperations = this.getMongoOperations();
        MongoEntityInformation<T, ID> entityInformation = this.getEntityInformation();
        Class<T> javaType = entityInformation.getJavaType();
        String collectionName = entityInformation.getCollectionName();
        return Optional.ofNullable(mongoOperations.findOne(query, javaType, collectionName));
    }
}
