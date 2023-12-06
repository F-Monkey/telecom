package cn.telecom.orm.ldap;

import cn.telecom.commons.data.QueryRequest;
import cn.telecom.orm.QueryRequestRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.ldap.core.LdapOperations;
import org.springframework.ldap.core.support.CountNameClassPairCallbackHandler;
import org.springframework.ldap.filter.Filter;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;

import java.util.List;
import java.util.Optional;

import static cn.telecom.orm.ldap.Repositories.OBJECT_CLASS_ATTRIBUTE;
import static org.springframework.ldap.query.LdapQueryBuilder.query;

@NoRepositoryBean
public interface LdapQueryRequestRepository<Q extends QueryRequest, T, ID> extends LdapCrudRepository<T, ID>, QueryRequestRepository<Q, T, ID> {
    default LdapQuery buildQuery(Q queryRequest) {
        return LdapQueryBuilder.query();
    }

    @Override
    default List<T> selectByQueryRequest(Q q, Sort sort) {
        LdapOperations operations = this.getOperations();
        Class<T> type = this.getDomainType();
        LdapQuery ldapQuery = buildQuery(q);
        return operations.find(ldapQuery, type);
    }

    @Override
    default Optional<T> selectOneByQueryRequest(Q q) {
        LdapOperations operations = this.getOperations();
        Class<T> type = this.getDomainType();
        LdapQuery ldapQuery = buildQuery(q);
        return Optional.ofNullable(operations.findOne(ldapQuery, type));
    }

    @Override
    default Page<T> selectPageByQueryRequest(Q q, Pageable pageable) {
        LdapOperations operations = this.getOperations();
        LdapQuery ldapQuery = this.buildQuery(q);
        Filter filter = ldapQuery.filter();
        CountNameClassPairCallbackHandler callback = new CountNameClassPairCallbackHandler();
        LdapQuery query = query().attributes(OBJECT_CLASS_ATTRIBUTE).filter(filter);
        operations.search(query, callback);
        List<T> list = operations.find(ldapQuery, this.getDomainType());
        return PageableExecutionUtils.getPage(list, pageable, callback::getNoOfRows);
    }
}
