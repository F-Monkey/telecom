package cn.telecom.orm.jpa;

import cn.telecom.commons.data.QueryRequest;
import cn.telecom.orm.QueryRequestRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface JpaQueryRequestRepository<Q extends QueryRequest, T, ID> extends QueryRequestRepository<Q, T, ID>, JpaCrudRepository<T, ID> {
    default Specification<T> buildQuery(Q q) {
        return (root, query, criteriaBuilder) -> query.getRestriction();
    }

    @Override
    default List<T> selectByQueryRequest(Q q, Sort sort) {
        Specification<T> specification = buildQuery(q);
        return findAll(specification, sort);
    }

    @Override
    default Page<T> selectPageByQueryRequest(Q q, Pageable pageable) {
        Specification<T> specification = buildQuery(q);
        return findAll(specification, pageable);
    }

    @Override
    default Optional<T> selectOneByQueryRequest(Q q) {
        Specification<T> specification = buildQuery(q);
        return findOne(specification);
    }
}
