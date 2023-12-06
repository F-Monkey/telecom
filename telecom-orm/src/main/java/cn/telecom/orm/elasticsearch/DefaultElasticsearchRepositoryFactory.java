package cn.telecom.orm.elasticsearch;

import cn.telecom.orm.BaseEntityCrudRepository;
import cn.telecom.orm.IRepository;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchEntityInformationCreator;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchEntityInformationCreatorImpl;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchRepositoryFactory;
import org.springframework.data.elasticsearch.repository.support.SimpleElasticsearchRepository;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.lang.NonNull;

public class DefaultElasticsearchRepositoryFactory extends ElasticsearchRepositoryFactory implements ApplicationContextAware {
    protected final ElasticsearchOperations elasticsearchOperations;
    protected final ElasticsearchEntityInformationCreator entityInformationCreator;

    protected ApplicationContext applicationContext;

    public DefaultElasticsearchRepositoryFactory(ElasticsearchOperations elasticsearchOperations) {
        super(elasticsearchOperations);
        this.elasticsearchOperations = elasticsearchOperations;
        this.entityInformationCreator = new ElasticsearchEntityInformationCreatorImpl(
                elasticsearchOperations.getElasticsearchConverter().getMappingContext());
    }

    @Override
    @NonNull
    protected Class<?> getRepositoryBaseClass(@NonNull RepositoryMetadata metadata) {
        Class<?> repositoryInterface = metadata.getRepositoryInterface();
        if (BaseEntityCrudRepository.class.isAssignableFrom(repositoryInterface)) {
            return SimpleElasticsearchBaseEntityRepository.class;
        }
        if (IRepository.class.isAssignableFrom(repositoryInterface)) {
            return SimpleElasticsearchCrudRepository.class;
        }
        return super.getRepositoryBaseClass(metadata);
    }

    @Override
    protected Object getTargetRepository(RepositoryInformation metadata) {
        Object targetRepository = super.getTargetRepository(metadata);
        if (targetRepository instanceof SimpleElasticsearchCrudRepository<?, ?> simpleElasticsearchRepository) {
            simpleElasticsearchRepository.setApplicationContext(this.applicationContext);
            try {
                simpleElasticsearchRepository.afterPropertiesSet();
            } catch (Exception ignore) {
            }
        }
        return targetRepository;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
