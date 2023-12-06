package cn.telecom.spring.bean.reactive;

import cn.telecom.commons.data.DataStatus;
import cn.telecom.commons.data.EntityMapper;
import cn.telecom.commons.data.QueryRequest;
import cn.telecom.commons.data.vo.Result;
import cn.telecom.commons.data.vo.Results;
import cn.telecom.commons.util.ClassUtil;
import cn.telecom.orm.reactive.ReactiveQueryRequestRepository;
import cn.telecom.spring.web.data.ExtensionQueryRequest;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

import java.util.Arrays;


public abstract class AbstractReactiveCrudService<Q extends QueryRequest, T, D, R, Repository extends ReactiveQueryRequestRepository<Q, T, String>, Mapper extends EntityMapper<D, T, R>> implements ReactiveCrudService<Q, D, R> {
    protected final Repository requestRepository;
    protected final Mapper entityMapper;

    public AbstractReactiveCrudService(Repository requestRepository) {
        this(requestRepository, null);
    }

    public AbstractReactiveCrudService(Repository requestRepository, Mapper mapper) {
        this.requestRepository = requestRepository;
        if (mapper == null) {
            Class<Mapper> mapperC = ClassUtil.getActualType(this, AbstractReactiveCrudService.class, "Mapper");
            this.entityMapper = Mappers.getMapper(mapperC);
        } else {
            this.entityMapper = mapper;
        }
    }

    @Override
    public Mono<Result<Page<R>>> read(ExtensionQueryRequest request, Q queryRequest, Pageable pageable) {
        return this.requestRepository.selectPageByQueryRequest(queryRequest, pageable)
                .map(page -> page.map(this.entityMapper::copyToVo))
                .map(Results::ok);
    }

    @Override
    public Mono<Result<R>> read(ExtensionQueryRequest request, String id) {
        return this.requestRepository.findById(id)
                .map(this.entityMapper::copyToVo)
                .map(Results::ok)
                .switchIfEmpty(Mono.just(Results.fail("can not find by id: " + id)));
    }

    @Override
    public Mono<Result<R>> read(ExtensionQueryRequest request, Q queryRequest) {
        return this.requestRepository.selectOneByQueryRequest(queryRequest)
                .map(this.entityMapper::copyToVo)
                .map(Results::ok)
                .switchIfEmpty(Mono.just(Results.fail("can not find by queryRequest")));
    }

    @Override
    public Mono<Result<R>> create(ExtensionQueryRequest queryRequest, D dto) {
        T t = this.entityMapper.copyFromDto(dto);
        return this.requestRepository.save(t)
                .map(this.entityMapper::copyToVo)
                .map(Results::ok);
    }

    @Override
    public Mono<Result<R>> update(ExtensionQueryRequest queryRequest, String id, D dto) {
        return this.requestRepository.findById(id)
                .map(t -> {
                    T tt = this.entityMapper.copyFromDto(dto);
                    return this.entityMapper.mergeNoNullVal(t, tt);
                })
                .flatMap(this.requestRepository::save)
                .map(this.entityMapper::copyToVo)
                .map(Results::ok)
                .switchIfEmpty(Mono.just(Results.fail("can not find by id: " + id)));
    }

    @Override
    public Mono<Result<Void>> delete(ExtensionQueryRequest queryRequest, String... id) {
        return this.requestRepository.deleteAllById(Arrays.asList(id))
                .then(Mono.just(Results.ok()));
    }

}
