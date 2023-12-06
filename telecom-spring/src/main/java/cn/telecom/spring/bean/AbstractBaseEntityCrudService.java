package cn.telecom.spring.bean;

import cn.telecom.commons.data.BaseEntity;
import cn.telecom.commons.data.DataStatus;
import cn.telecom.commons.data.EntityMapper;
import cn.telecom.commons.data.QueryRequest;
import cn.telecom.commons.data.vo.Result;
import cn.telecom.commons.data.vo.Results;
import cn.telecom.orm.QueryRequestRepository;
import cn.telecom.spring.web.data.ExtensionQueryRequest;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AbstractBaseEntityCrudService<Q extends QueryRequest, T extends BaseEntity, D, R, Repository extends QueryRequestRepository<Q, T, String>, Mapper extends EntityMapper<D, T, R>> extends
        AbstractCrudService<Q, T, D, R, Repository, Mapper> {
    public AbstractBaseEntityCrudService(Repository requestRepository) {
        super(requestRepository);
    }

    public AbstractBaseEntityCrudService(Repository requestRepository, Mapper mapper) {
        super(requestRepository, mapper);
    }

    @Override
    public Result<Void> delete(ExtensionQueryRequest queryRequest, String... id) {
        List<T> list = this.requestRepository.findAllById(Arrays.asList(id));
        if (CollectionUtils.isEmpty(list)) {
            return Results.ok();
        }
        List<T> collect = list.stream().peek(t -> t.setDataStatus(DataStatus.DELETED.getCode())).collect(Collectors.toList());
        this.requestRepository.saveAll(collect);
        return Results.ok();
    }
}
