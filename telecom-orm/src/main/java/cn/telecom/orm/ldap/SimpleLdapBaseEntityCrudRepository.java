package cn.telecom.orm.ldap;

import cn.telecom.commons.data.BaseEntity;
import cn.telecom.orm.BaseEntityBeforeCreateBehavior;
import cn.telecom.orm.BaseEntityBeforeUpdateBehavior;
import cn.telecom.orm.BeforeCreateBehavior;
import cn.telecom.orm.BeforeUpdateBehavior;
import org.springframework.data.ldap.repository.support.SimpleLdapRepository;
import org.springframework.ldap.core.LdapOperations;
import org.springframework.ldap.odm.core.ObjectDirectoryMapper;
import org.springframework.util.Assert;

import javax.naming.Name;


public class SimpleLdapBaseEntityCrudRepository<T extends BaseEntity> extends SimpleLdapCrudRepository<T> implements LdapBaseEntityCrudRepository<T> {


    private BeforeUpdateBehavior<BaseEntity> beforeUpdateBehavior;

    private BeforeCreateBehavior<BaseEntity> beforeCreateBehavior;

    /**
     * Creates a new {@link SimpleLdapRepository}.
     *
     * @param ldapOperations must not be {@literal null}.
     * @param odm            must not be {@literal null}.
     * @param entityType     must not be {@literal null}.
     */
    public SimpleLdapBaseEntityCrudRepository(LdapOperations ldapOperations, ObjectDirectoryMapper odm, Class<T> entityType) {
        super(ldapOperations, odm, entityType);
    }

    @Override
    public void afterPropertiesSet() {
        if (this.applicationContext == null) {
            return;
        }
        try {
            this.beforeUpdateBehavior = this.applicationContext.getBean(BaseEntityBeforeUpdateBehavior.class);
            this.beforeCreateBehavior = this.applicationContext.getBean(BaseEntityBeforeCreateBehavior.class);
        } catch (Exception ignore) {
        }
    }

    @Override
    public <S extends T> S save(S entity) {
        Assert.notNull(entity, "Entity must not be null");

        Name declaredId = this.getOdm().getId(entity);

        if (isNew(entity, declaredId)) {
            if (this.beforeCreateBehavior != null) {
                this.beforeCreateBehavior.beforeCreate(entity);
            }
            this.getOperations().create(entity);
        } else {
            if (this.beforeUpdateBehavior != null) {
                this.beforeUpdateBehavior.beforeUpdate(entity);
            }
            this.getOperations().update(entity);
        }

        return entity;
    }
}
