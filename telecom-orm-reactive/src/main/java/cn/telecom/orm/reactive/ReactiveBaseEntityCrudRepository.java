package cn.telecom.orm.reactive;

import cn.telecom.commons.data.BaseEntity;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ReactiveBaseEntityCrudRepository<T extends BaseEntity> extends IReactiveRepository<T, String> {

}
