package cn.telecom.spring.bean.reactive;

import cn.telecom.commons.data.BaseEntity;
import cn.telecom.commons.data.DataStatus;
import cn.telecom.commons.data.EntityMapper;
import cn.telecom.commons.data.QueryRequest;
import cn.telecom.commons.data.vo.Result;
import cn.telecom.commons.data.vo.Results;
import cn.telecom.orm.reactive.ReactiveQueryRequestRepository;
import cn.telecom.spring.web.data.ExtensionQueryRequest;
import reactor.core.publisher.Mono;

import java.util.Arrays;

public class AbstractReactiveBaseEntityCrudService<Q extends QueryRequest, T extends BaseEntity, D, R, Repository extends ReactiveQueryRequestRepository<Q, T, String>, Mapper extends EntityMapper<D, T, R>>
        extends AbstractReactiveCrudService<Q, T, D, R, Repository, Mapper> {
    public AbstractReactiveBaseEntityCrudService(Repository repository) {
        super(repository);
    }

    public AbstractReactiveBaseEntityCrudService(Repository repository, Mapper mapper) {
        super(repository, mapper);
    }

    @Override
    public Mono<Result<Void>> delete(ExtensionQueryRequest queryRequest, String... id) {
        return this.requestRepository.findAllById(Arrays.asList(id))
                .doOnNext(t -> t.setDataStatus(DataStatus.DELETED.getCode()))
                .collectList()
                .flatMap(list -> this.requestRepository.saveAll(list).then())
                .map(Results::ok)
                .switchIfEmpty(Mono.just(Results.ok()));
    }
}
