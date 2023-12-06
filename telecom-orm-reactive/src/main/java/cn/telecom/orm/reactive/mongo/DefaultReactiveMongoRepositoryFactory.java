package cn.telecom.orm.reactive.mongo;

import cn.telecom.orm.reactive.IReactiveRepository;
import cn.telecom.orm.reactive.ReactiveBaseEntityCrudRepository;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.repository.support.ReactiveMongoRepositoryFactory;
import org.springframework.data.mongodb.repository.support.SimpleReactiveMongoRepository;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryMethodEvaluationContextProvider;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Optional;

public class DefaultReactiveMongoRepositoryFactory extends ReactiveMongoRepositoryFactory implements ApplicationContextAware {

    protected ApplicationContext applicationContext;

    /**
     * Creates a new {@link ReactiveMongoRepositoryFactory} with the given {@link ReactiveMongoOperations}.
     *
     * @param mongoOperations must not be {@literal null}.
     */
    public DefaultReactiveMongoRepositoryFactory(ReactiveMongoOperations mongoOperations) {
        super(mongoOperations);
    }

    @Override
    @NonNull
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        Class<?> repositoryInterface = metadata.getRepositoryInterface();
        if (ReactiveBaseEntityCrudRepository.class.isAssignableFrom(repositoryInterface)) {
            return SimpleReactiveMongoBaseEntityRepository.class;
        }
        if (IReactiveRepository.class.isAssignableFrom(repositoryInterface)) {
            return SimpleReactiveMongoCrudRepository.class;
        }
        return SimpleReactiveMongoRepository.class;
    }

    @Override
    protected Object getTargetRepository(RepositoryInformation information) {
        Object targetRepository = super.getTargetRepository(information);
        if (targetRepository instanceof SimpleReactiveMongoCrudRepository<?, ?> repository) {
            repository.setApplicationContext(this.applicationContext);
            try {
                repository.afterPropertiesSet();
            } catch (Exception ignore) {
            }
        }
        return targetRepository;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
