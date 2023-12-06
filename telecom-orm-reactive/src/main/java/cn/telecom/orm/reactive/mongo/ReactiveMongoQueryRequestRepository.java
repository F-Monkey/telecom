package cn.telecom.orm.reactive.mongo;

import cn.telecom.commons.data.QueryRequest;
import cn.telecom.orm.reactive.ReactivePageableExecutionUtils;
import cn.telecom.orm.reactive.ReactiveQueryRequestRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.repository.NoRepositoryBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@NoRepositoryBean
public interface ReactiveMongoQueryRequestRepository<Q extends QueryRequest, T, ID> extends ReactiveQueryRequestRepository<Q, T, ID>, ReactiveMongoCrudRepository<T, ID> {
    default Query buildQuery(Q q) {
        return new Query();
    }

    @Override
    default Mono<Page<T>> selectPageByQueryRequest(Q q, Pageable pageable) {
        Query query = this.buildQuery(q);
        query.with(pageable);
        MongoEntityInformation<T, ID> entityInformation = this.getEntityInformation();
        Class<T> type = entityInformation.getJavaType();
        String collectionName = entityInformation.getCollectionName();
        ReactiveMongoOperations mongoOperations = this.getMongoOperations();
        Flux<T> flux = mongoOperations.find(query, type, collectionName);
        return flux.collectList().flatMap(list -> ReactivePageableExecutionUtils.getPage(list, pageable, mongoOperations
                .count(Query.of(query).limit(-1).skip(-1), type, collectionName)));
    }

    @Override
    default Flux<T> selectByQueryRequest(Q q, Sort sort) {
        Query build = this.buildQuery(q);
        ReactiveMongoOperations mongoOperations = this.getMongoOperations();
        MongoEntityInformation<T, ID> entityInformation = this.getEntityInformation();
        Class<T> javaType = entityInformation.getJavaType();
        String collectionName = entityInformation.getCollectionName();
        return mongoOperations.find(build, javaType, collectionName);
    }

    @Override
    default Mono<T> selectOneByQueryRequest(Q q) {
        Query query = this.buildQuery(q);
        ReactiveMongoOperations mongoOperations = this.getMongoOperations();
        MongoEntityInformation<T, ID> entityInformation = this.getEntityInformation();
        Class<T> javaType = entityInformation.getJavaType();
        String collectionName = entityInformation.getCollectionName();
        return mongoOperations.findOne(query, javaType, collectionName);
    }
}