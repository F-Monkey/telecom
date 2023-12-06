package cn.telecom.orm.ldap;

import cn.telecom.commons.data.BaseEntity;
import cn.telecom.orm.BaseEntityCrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface LdapBaseEntityCrudRepository<T extends BaseEntity> extends BaseEntityCrudRepository<T>, LdapCrudRepository<T, String> {
}
