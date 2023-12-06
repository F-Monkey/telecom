package cn.telecom.orm.ldap;

import cn.telecom.orm.IRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.ldap.core.LdapOperations;
import org.springframework.ldap.odm.core.ObjectDirectoryMapper;
import org.springframework.ldap.query.LdapQuery;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface LdapCrudRepository<T, ID> extends IRepository<T, ID> {
    LdapOperations getOperations();

    Class<T> getDomainType();

    ObjectDirectoryMapper getOdm();

    /**
     * Find one entry matching the specified query.
     *
     * @param ldapQuery the query specification.
     * @return the found entry or <code>null</code> if no matching entry was found.
     * @throws org.springframework.dao.IncorrectResultSizeDataAccessException if more than one entry matches the query.
     */
    Optional<T> findOne(LdapQuery ldapQuery);

    /**
     * Find all entries matching the specified query.
     *
     * @param ldapQuery the query specification.
     * @return the entries matching the query.
     */
    List<T> findAll(LdapQuery ldapQuery);
}
