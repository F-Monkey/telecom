package cn.telecom.spring.bean;


import cn.telecom.commons.data.QueryRequest;
import cn.telecom.spring.web.data.ExtensionQueryRequest;
import cn.telecom.commons.data.vo.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CrudService<Q extends QueryRequest, D, R> {

    default Result<R> read(ExtensionQueryRequest authQueryRequest, String id) {
        throw new UnsupportedOperationException();
    }

    default Result<R> read(ExtensionQueryRequest authQueryRequest, Q queryRequest) {
        throw new UnsupportedOperationException();
    }

    default Result<Page<R>> read(ExtensionQueryRequest authQueryRequest, Q queryRequest, Pageable pageable) {
        throw new UnsupportedOperationException();
    }

    default Result<R> create(ExtensionQueryRequest authQueryRequest, D dto) {
        throw new UnsupportedOperationException();
    }

    default Result<R> update(ExtensionQueryRequest authQueryRequest, String id, D dto) {
        throw new UnsupportedOperationException();
    }

    default Result<Void> delete(ExtensionQueryRequest authQueryRequest, String... id) {
        throw new UnsupportedOperationException();
    }

}