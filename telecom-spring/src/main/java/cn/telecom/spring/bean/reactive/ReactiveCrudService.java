package cn.telecom.spring.bean.reactive;

import cn.telecom.commons.data.QueryRequest;
import cn.telecom.commons.data.vo.Result;
import cn.telecom.spring.web.data.ExtensionQueryRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

public interface ReactiveCrudService<Q extends QueryRequest, D, R> {

    default Mono<Result<R>> read(ExtensionQueryRequest authQueryRequest, String id) {
        return Mono.error(new UnsupportedOperationException());
    }

    default Mono<Result<R>> read(ExtensionQueryRequest authQueryRequest, Q queryRequest) {
        return Mono.error(new UnsupportedOperationException());
    }

    default Mono<Result<Page<R>>> read(ExtensionQueryRequest authQueryRequest, Q queryRequest, Pageable pageable) {
        return Mono.error(new UnsupportedOperationException());
    }

    default Mono<Result<R>> create(ExtensionQueryRequest authQueryRequest, D dto) {
        return Mono.error(new UnsupportedOperationException());
    }

    default Mono<Result<R>> update(ExtensionQueryRequest authQueryRequest, String id, D dto) {
        return Mono.error(new UnsupportedOperationException());
    }

    default Mono<Result<Void>> delete(ExtensionQueryRequest authQueryRequest, String... id) {
        return Mono.error(new UnsupportedOperationException());
    }

}
