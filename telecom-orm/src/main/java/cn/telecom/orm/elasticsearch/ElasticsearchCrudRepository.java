package cn.telecom.orm.elasticsearch;

import cn.telecom.orm.IRepository;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchEntityInformation;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ElasticsearchCrudRepository<T, ID> extends ElasticsearchRepository<T, ID>, IRepository<T, ID> {
    ElasticsearchEntityInformation<T, ?> getEntityInformation();

    ElasticsearchOperations getOperations();
}
