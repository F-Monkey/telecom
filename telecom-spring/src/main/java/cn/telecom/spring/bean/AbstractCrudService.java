package cn.telecom.spring.bean;

import cn.telecom.orm.QueryRequestRepository;
import cn.telecom.spring.web.data.ExtensionQueryRequest;
import cn.telecom.commons.data.*;
import cn.telecom.commons.data.vo.Result;
import cn.telecom.commons.data.vo.Results;
import cn.telecom.commons.util.ClassUtil;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

public abstract class AbstractCrudService<Q extends QueryRequest, T, D, R, Repository extends QueryRequestRepository<Q, T, String>, Mapper extends EntityMapper<D, T, R>> implements CrudService<Q, D, R> {
    protected final Repository requestRepository;

    protected final Mapper entityMapper;

    public AbstractCrudService(Repository requestRepository) {
        this(requestRepository, null);
    }

    public AbstractCrudService(Repository requestRepository, Mapper mapper) {
        this.requestRepository = requestRepository;
        if (mapper == null) {
            Class<Mapper> mapperC = ClassUtil.getActualType(this, AbstractCrudService.class, "Mapper");
            this.entityMapper = Mappers.getMapper(mapperC);
        } else {
            this.entityMapper = mapper;
        }
    }

    @Override
    public Result<Page<R>> read(ExtensionQueryRequest request, Q queryRequest, Pageable pageable) {
        Page<T> page = this.requestRepository.selectPageByQueryRequest(queryRequest, pageable);
        return Results.ok(page.map(this.entityMapper::copyToVo));
    }

    @Override
    public Result<R> read(ExtensionQueryRequest request, String id) {
        return this.requestRepository.findById(id)
                .map(this.entityMapper::copyToVo)
                .map(Results::ok)
                .orElse(Results.fail("can not find by id:" + id));
    }

    @Override
    public Result<R> read(ExtensionQueryRequest request, Q queryRequest) {
        return this.requestRepository.selectOneByQueryRequest(queryRequest)
                .map(this.entityMapper::copyToVo)
                .map(Results::ok)
                .orElse(Results.fail("can not find by queryRequest"));
    }

    @Override
    public Result<R> create(ExtensionQueryRequest queryRequest, D dto) {
        T t = this.entityMapper.copyFromDto(dto);
        T insert = this.requestRepository.save(t);
        return Results.ok(this.entityMapper.copyToVo(insert));
    }

    @Override
    public Result<R> update(ExtensionQueryRequest queryRequest, String id, D dto) {
        Optional<T> optional = this.requestRepository.findById(id);
        if (optional.isEmpty()) {
            return Results.fail("can not find by id:" + id);
        }
        T t = this.entityMapper.copyFromDto(dto);
        t = this.entityMapper.mergeNoNullVal(optional.get(), t);
        T save = this.requestRepository.save(t);
        return Results.ok(this.entityMapper.copyToVo(save));
    }

    @Override
    public Result<Void> delete(ExtensionQueryRequest queryRequest, String... id) {
        this.requestRepository.deleteAllById(Arrays.asList(id));
        return Results.ok();
    }
}