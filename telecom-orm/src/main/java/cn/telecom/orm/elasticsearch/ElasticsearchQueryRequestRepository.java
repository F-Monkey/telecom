package cn.telecom.orm.elasticsearch;

import cn.telecom.commons.data.QueryRequest;
import cn.telecom.orm.QueryRequestRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface ElasticsearchQueryRequestRepository<Q extends QueryRequest, T, ID> extends QueryRequestRepository<Q, T, ID>, ElasticsearchCrudRepository<T, ID> {

    CriteriaQuery criteriaQuery(Q q);


    @Override
    default List<T> selectByQueryRequest(Q q, Sort sort) {
        ElasticsearchOperations operations = this.getOperations();
        CriteriaQuery criteriaQuery = this.criteriaQuery(q);
        SearchHits<T> search = operations.search(criteriaQuery, this.getEntityInformation().getJavaType());
        return search.getSearchHits().stream().map(SearchHit::getContent).toList();
    }


    @Override
    default Page<T> selectPageByQueryRequest(Q q, Pageable pageable) {
        CriteriaQuery criteriaQuery = this.criteriaQuery(q);

        ElasticsearchOperations operations = this.getOperations();
        Query query = criteriaQuery.setPageable(pageable);
        SearchHits<T> search = operations.search(query, this.getEntityInformation().getJavaType());
        Query countQuery = criteriaQuery.setPageable(Pageable.unpaged());
        Class<T> javaType = this.getEntityInformation().getJavaType();
        return PageableExecutionUtils.getPage(search.getSearchHits().stream().map(SearchHit::getContent).toList()
                , pageable, () -> operations.count(countQuery, javaType));
    }

    @Override
    default Optional<T> selectOneByQueryRequest(Q q) {
    }
}
