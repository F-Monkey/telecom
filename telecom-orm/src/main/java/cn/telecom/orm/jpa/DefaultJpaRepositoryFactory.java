package cn.telecom.orm.jpa;

import cn.telecom.orm.BaseEntityCrudRepository;
import cn.telecom.orm.IRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;

public class DefaultJpaRepositoryFactory extends JpaRepositoryFactory implements ApplicationContextAware {
    /**
     * Creates a new {@link JpaRepositoryFactory}.
     *
     * @param entityManager must not be {@literal null}
     */
    public DefaultJpaRepositoryFactory(EntityManager entityManager) {
        super(entityManager);
    }

    protected ApplicationContext applicationContext;

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        Class<?> repositoryInterface = metadata.getRepositoryInterface();
        if (BaseEntityCrudRepository.class.isAssignableFrom(repositoryInterface)) {
            return SimpleJpaBaseEntityRepository.class;
        }
        if (IRepository.class.isAssignableFrom(repositoryInterface)) {
            return SimpleJpaCrudRepository.class;
        }
        return super.getRepositoryBaseClass(metadata);
    }

    @Override
    protected JpaRepositoryImplementation<?, ?> getTargetRepository(RepositoryInformation information, EntityManager entityManager) {
        JpaRepositoryImplementation<?, ?> targetRepository = super.getTargetRepository(information, entityManager);
        if (targetRepository instanceof SimpleJpaCrudRepository<?, ?> repository) {
            repository.setApplicationContext(this.applicationContext);
            try {
                repository.afterPropertiesSet();
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
