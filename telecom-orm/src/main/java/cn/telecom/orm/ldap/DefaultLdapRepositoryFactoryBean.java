package cn.telecom.orm.ldap;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.ldap.repository.support.LdapRepositoryFactoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.naming.Name;

public class DefaultLdapRepositoryFactoryBean<T extends Repository<S, Name>, S> extends LdapRepositoryFactoryBean<T, S>
        implements ApplicationContextAware {

    protected ApplicationContext applicationContext;

    /**
     * Creates a new {@link LdapRepositoryFactoryBean} for the given repository interface.
     *
     * @param repositoryInterface must not be {@literal null}.
     */
    public DefaultLdapRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
        super(repositoryInterface);
    }


    @Override
    protected RepositoryFactorySupport createRepositoryFactory() {
        RepositoryFactorySupport repositoryFactory = super.createRepositoryFactory();
        if (repositoryFactory instanceof DefaultLdapRepositoryFactory defaultLdapRepositoryFactory) {
            defaultLdapRepositoryFactory.setApplicationContext(this.applicationContext);
        }
        return repositoryFactory;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
