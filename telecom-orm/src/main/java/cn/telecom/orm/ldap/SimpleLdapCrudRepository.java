package cn.telecom.orm.ldap;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.*;
import org.springframework.data.ldap.repository.support.SimpleLdapRepository;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.data.util.Optionals;
import org.springframework.lang.Nullable;
import org.springframework.ldap.NameNotFoundException;
import org.springframework.ldap.core.LdapOperations;
import org.springframework.ldap.core.support.CountNameClassPairCallbackHandler;
import org.springframework.ldap.filter.Filter;
import org.springframework.ldap.odm.core.ObjectDirectoryMapper;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.util.Assert;

import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.ldap.LdapName;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static cn.telecom.orm.ldap.Repositories.OBJECT_CLASS_ATTRIBUTE;
import static org.springframework.ldap.query.LdapQueryBuilder.query;

public class SimpleLdapCrudRepository<T> implements LdapCrudRepository<T, String>, ApplicationContextAware, InitializingBean {

    private final LdapOperations ldapOperations;
    private final ObjectDirectoryMapper odm;
    private final Class<T> entityType;

    protected ApplicationContext applicationContext;

    /**
     * Creates a new {@link SimpleLdapRepository}.
     *
     * @param ldapOperations must not be {@literal null}.
     * @param odm            must not be {@literal null}.
     * @param entityType     must not be {@literal null}.
     */
    public SimpleLdapCrudRepository(LdapOperations ldapOperations, ObjectDirectoryMapper odm, Class<T> entityType) {

        Assert.notNull(ldapOperations, "LdapOperations must not be null");
        Assert.notNull(odm, "ObjectDirectoryMapper must not be null");
        Assert.notNull(entityType, "Entity type must not be null");

        this.ldapOperations = ldapOperations;
        this.odm = odm;
        this.entityType = entityType;
    }
    // -------------------------------------------------------------------------
    // Methods from CrudRepository
    // -------------------------------------------------------------------------

    @Override
    public <S extends T> S save(S entity) {

        Assert.notNull(entity, "Entity must not be null");

        Name declaredId = odm.getId(entity);

        if (isNew(entity, declaredId)) {
            ldapOperations.create(entity);
        } else {
            ldapOperations.update(entity);
        }

        return entity;
    }

    @Override
    public <S extends T> List<S> saveAll(Iterable<S> entities) {

        return StreamSupport.stream(entities.spliterator(), false) //
                .map(this::save) //
                .collect(Collectors.toList());
    }

    @Override
    public Optional<T> findById(String name) {
        Assert.notNull(name, "Id must not be null");
        try {
            LdapName ldapName = new LdapName(name);
            return Optional.ofNullable(ldapOperations.findByDn(ldapName, entityType));
        } catch (NameNotFoundException | InvalidNameException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean existsById(String name) {
        Assert.notNull(name, "Id must not be null");
        return findById(name).isPresent();
    }

    @Override
    public List<T> findAll() {
        return ldapOperations.findAll(entityType);
    }

    @Override
    public List<T> findAllById(Iterable<String> names) {

        return StreamSupport.stream(names.spliterator(), false) //
                .map(this::findById) //
                .flatMap(Optionals::toStream) //
                .collect(Collectors.toList());
    }

    @Override
    public long count() {

        Filter filter = odm.filterFor(entityType, null);
        CountNameClassPairCallbackHandler callback = new CountNameClassPairCallbackHandler();
        LdapQuery query = query().attributes(OBJECT_CLASS_ATTRIBUTE).filter(filter);
        ldapOperations.search(query, callback);

        return callback.getNoOfRows();
    }

    @Override
    public void deleteById(String name) {
        Assert.notNull(name, "Id must not be null");
        ldapOperations.unbind(name);
    }

    @Override
    public void delete(T entity) {

        Assert.notNull(entity, "Entity must not be null");

        ldapOperations.delete(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends String> names) {
        Assert.notNull(names, "Names must not be null");
        names.forEach(this::deleteById);
    }

    @Override
    public void deleteAll(Iterable<? extends T> entities) {

        Assert.notNull(entities, "Entities must not be null");

        entities.forEach(this::delete);
    }

    @Override
    public void deleteAll() {
        deleteAll(findAll());
    }

    // -------------------------------------------------------------------------
    // Methods from LdapRepository
    // ------------------------------------------------------------------------

    @Override
    public Optional<T> findOne(LdapQuery ldapQuery) {

        Assert.notNull(ldapQuery, "LdapQuery must not be null");

        try {
            return Optional.ofNullable(ldapOperations.findOne(ldapQuery, entityType));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<T> findAll(LdapQuery ldapQuery) {

        Assert.notNull(ldapQuery, "LdapQuery must not be null");
        return ldapOperations.find(ldapQuery, entityType);
    }


    protected <S extends T> boolean isNew(S entity, @Nullable Name id) {

        if (entity instanceof Persistable<?> persistable) {
            return persistable.isNew();
        } else {
            return id == null;
        }
    }

    @Override
    public LdapOperations getOperations() {
        return this.ldapOperations;
    }

    @Override
    public Class<T> getDomainType() {
        return this.entityType;
    }

    @Override
    public ObjectDirectoryMapper getOdm() {
        return this.odm;
    }

    @Override
    public List<T> findAll(Sort sort) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        throw new UnsupportedOperationException();
    }
    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
