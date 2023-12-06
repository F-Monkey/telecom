package cn.telecom.orm;

import cn.telecom.commons.data.QueryRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface QueryRequestRepository<Q extends QueryRequest, PO, ID> extends IRepository<PO, ID> {

    default Page<PO> selectPageByQueryRequest(Q q, Pageable pageable) {
        throw new UnsupportedOperationException();
    }

    default List<PO> selectByQueryRequest(Q q, Sort sort) {
        throw new UnsupportedOperationException();
    }

    default Optional<PO> selectOneByQueryRequest(Q q) {
        throw new UnsupportedOperationException();
    }
}