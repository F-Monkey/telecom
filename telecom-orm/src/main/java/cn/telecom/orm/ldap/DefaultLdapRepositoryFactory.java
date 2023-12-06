package cn.telecom.orm.ldap;

import cn.telecom.orm.BaseEntityCrudRepository;
import cn.telecom.orm.IRepository;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.ldap.repository.support.LdapRepositoryFactory;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.ldap.core.LdapOperations;

public class DefaultLdapRepositoryFactory extends LdapRepositoryFactory implements ApplicationContextAware {

    protected ApplicationContext applicationContext;

    /**
     * Creates a new {@link LdapRepositoryFactory}.
     *
     * @param ldapOperations must not be {@literal null}.
     */
    public DefaultLdapRepositoryFactory(LdapOperations ldapOperations) {
        super(ldapOperations);
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        Class<?> repositoryInterface = metadata.getRepositoryInterface();
        if (BaseEntityCrudRepository.class.isAssignableFrom(repositoryInterface)) {
            return SimpleLdapBaseEntityCrudRepository.class;
        }
        if (IRepository.class.isAssignableFrom(repositoryInterface)) {
            return SimpleLdapCrudRepository.class;
        }
        return super.getRepositoryBaseClass(metadata);
    }

    @Override
    protected Object getTargetRepository(RepositoryInformation information) {
        Object targetRepository = super.getTargetRepository(information);
        if (targetRepository instanceof SimpleLdapCrudRepository<?> repository) {
            repository.setApplicationContext(this.applicationContext);
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
