package cn.telecom.orm.reactive;

import cn.telecom.commons.data.QueryRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface ReactiveQueryRequestRepository<Q extends QueryRequest, T, ID> extends IReactiveRepository<T, ID> {

    default Mono<Page<T>> selectPageByQueryRequest(Q q, Pageable pageable) {
        return Mono.error(new UnsupportedOperationException());
    }

    default Flux<T> selectByQueryRequest(Q q, Sort sort) {
        return Flux.error(new UnsupportedOperationException());
    }

    default Mono<T> selectOneByQueryRequest(Q q) {
        return Mono.error(new UnsupportedOperationException());
    }
}